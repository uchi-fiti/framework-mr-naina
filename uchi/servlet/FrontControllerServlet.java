package uchi.servlet;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import uchi.annotations.ResponseBody;
import uchi.exceptions.IllegalParameterException;
import uchi.exceptions.NoSuchRouteException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Part;
import java.io.*;
import java.nio.file.*;
import java.util.UUID;
import uchi.utils.MapFile;
import uchi.utils.Mapping;
import uchi.utils.MethodEnum;
import uchi.utils.ModelAndView;
import uchi.utils.UrlMethod;

public class FrontControllerServlet extends HttpServlet {
    private List <String> controllersName = new ArrayList<>();
    private Map <UrlMethod, Mapping> mappings = new HashMap<>();
    private Exception exception = null;
    // TSY MILA init satria efa misy anle context listener
    @Override 
    public void init() {
        controllersName = (List<String>) getServletContext().getAttribute("controllersName");
        mappings = (Map<UrlMethod, Mapping>) getServletContext().getAttribute("mappings");
        exception = (Exception) getServletContext().getAttribute("exception");
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter err = response.getWriter();
        try {
            processRequest(request, response);
        } catch (NoSuchRouteException e) {
            err.print(e);
        } catch (Exception e) {
            err.print(e);
            e.printStackTrace();
        }
        err.flush();
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter err = response.getWriter();
        try {
            processRequest(request, response);
        } catch (NoSuchRouteException e) {
            err.print(e);
        } catch (Exception e) {
            err.print(e);
            e.printStackTrace();
        }
        err.flush();
    }
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(exception != null) {
            throw exception;
        }
        Mapping m = getRequestMapping(request, mappings);
        Method method = m.getMethod();
        Object objectInstance = createController(m);

        Object [] parameterValues = resolveMethodParameterValues(request, method);
        // debugging Object parameter
        for (Object object : parameterValues) {
            if(!isSimpleType(object.getClass())) {
                for (Field field : object.getClass().getDeclaredFields()) {
                    try {
                        String fieldName = field.getName();

                        // Construct getter name: name -> getName
                        String getterName = "get"
                                + upperCaseFirstLetter(fieldName);

                        // Find and invoke getter
                        Method getter = object.getClass().getMethod(getterName);
                        Object value = getter.invoke(object);

                        System.out.println(fieldName + " = " + value);

                    } catch (Exception e) {
                        System.out.println(field.getName()
                                + " : " + e.getMessage());
                    }
                }
            }       
        }

        Object retour = method.invoke(objectInstance, parameterValues);

        handleControllerMethodResult(request, response, method, retour);
    }
    Mapping getRequestMapping(HttpServletRequest request, Map<UrlMethod, Mapping> mappings) throws NoSuchRouteException {
        String methodStr = request.getMethod();
        MethodEnum methodEnum = methodStr.equalsIgnoreCase("get") ? MethodEnum.GET : MethodEnum.POST;
        String route = request.getPathInfo();
        Mapping m = mappings.get(new UrlMethod(route, methodEnum));
        if(m != null) {
            return m;
        } else {
            throw new NoSuchRouteException(route, methodEnum, mappings);
        }
    }
    String upperCaseFirstLetter(String text) {
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
    Object [] resolveMethodParameterValues(HttpServletRequest request, Method method) throws Exception {
        Parameter[] parameters = method.getParameters();
        Object [] values = new Object[parameters.length];
        int i = 0;
        for(Parameter p : parameters) {
            Object v = null;
            if(isSimpleType(p.getType())) {

                v = convertParameterValue(request.getParameter(p.getName()), p.getType());

            }  else if (p.getType() == MapFile.class) {

                System.out.println(p.getName() + " is of type MapFile");

                v = resolveMapFile(request, p.getName());

            } else if (List.class.isAssignableFrom(p.getType())) {

                v = resolveList(request, p.getParameterizedType(), p.getName());

            } else {

                v = resolveObject(request, p.getType(), p.getName());

            }
            values[i] = v;
            i++;
        }
        return values;
    }
    Object resolveObject(HttpServletRequest request, Class<?> type, String prefix) throws Exception {

        Object instance = type.getDeclaredConstructor().newInstance();

        for (Field f : type.getDeclaredFields()) {
            String fieldName = f.getName();
            String fullName = prefix + "." + fieldName;

            Object value = null;

            if (isSimpleType(f.getType())) {
                String rawValue = request.getParameter(fullName);

                if (rawValue == null) {
                    continue;
                }

                value = convertParameterValue(rawValue, f.getType());

            } else if(List.class.isAssignableFrom(f.getType())) { 

                value = resolveList(request, f.getGenericType(), fullName);

            } else {

                value = resolveObject(request, f.getType(), fullName);

            }

            String setterName = "set" + upperCaseFirstLetter(fieldName);
            Method setter = type.getMethod(setterName, f.getType());

            setter.invoke(instance, value);
        }

        return instance;
    }


    Object resolveList(HttpServletRequest request, Type type, String prefix) throws Exception {

        ParameterizedType parameterizedType = (ParameterizedType) type;

        Type elementType = parameterizedType.getActualTypeArguments()[0];
        Class<?> elementClass = (Class<?>) elementType;

        List<Object> values = new ArrayList<>();

        int i = 0;

        while (hasParameterWithPrefix(request, prefix + "[" + i + "]")) {

            String elementName = prefix + "[" + i + "]";

            Object value;

            if (isSimpleType(elementClass)) {

                value = convertParameterValue(request.getParameter(elementName), elementClass);

            } else {

                value = resolveObject(request, elementClass, elementName);

            }

            values.add(value);
            i++;
        }

        return values;
    }
    boolean hasParameterWithPrefix(HttpServletRequest request, String prefix) {
        Enumeration<String> names = request.getParameterNames();

        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (name.equals(prefix)
                    || name.startsWith(prefix + ".")
                    || name.startsWith(prefix + "[")) {
                return true;
            }
        }

        return false;
    }
    Object convertParameterValue(String value, Class <?> type) throws IllegalParameterException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        if(type == String.class) {
            return value;
        } 
        if(type == int.class || type == Integer.class) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new IllegalParameterException(
                    "Invalid integer value: " + value
                );
            }
        }
        if(type == long.class || type == Long.class) {
            try {
                return Long.parseLong(value);
            } catch(NumberFormatException e) {
                 throw new IllegalParameterException(
                    "Invalid long value: " + value
                );
            }
        }
        if(type == double.class || type == Double.class) {
            try {
                return Double.parseDouble(value);
            } catch(NumberFormatException e) {
                 throw new IllegalParameterException(
                    "Invalid double value: " + value
                );
            }
        }

        return  null;
    }
    void handleControllerMethodResult(HttpServletRequest request, HttpServletResponse response, Method method, Object retour) throws ServletException, IOException {
        if(retour instanceof ModelAndView) {
            String prefix = getServletContext().getInitParameter("prefix");
            String suffix = getServletContext().getInitParameter("suffix");
            ModelAndView mv = (ModelAndView) retour;
            
            String view = prefix + mv.getView() + suffix;

            if (mv.getAttributes() != null) {
                for (Map.Entry<String, Object> entry : mv.getAttributes().entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
            }

            RequestDispatcher dispat = request.getRequestDispatcher(view);
            dispat.forward(request, response);
        } else if(method.isAnnotationPresent(ResponseBody.class)) {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");

            if(retour instanceof String){
                out.write((String) retour);
            } else {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(retour);
                out.write(json);
            }
        } 
    }

    Object createController(Mapping m) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return m.getController().getDeclaredConstructor().newInstance();
    }

    boolean isSimpleType(Class<?> type) {
        return type == String.class
            || type == int.class || type == Integer.class
            || type == long.class || type == Long.class
            || type == double.class || type == Double.class;
    }


    // Converts an uploaded file into our MapFile object.
    MapFile resolveMapFile(
            HttpServletRequest request,
            String parameterName
    ) throws Exception {

        Part part = request.getPart(parameterName);

        if (part == null || part.getSubmittedFileName() == null
                || part.getSubmittedFileName().isEmpty()) {
            return null;
        }

        // Retrieve the original filename.
        String originalName = part.getSubmittedFileName();

        // Remove any client-supplied directory components.
        originalName = originalName.replace('\\', '/');
        originalName = originalName.substring(
            originalName.lastIndexOf('/') + 1
        );

        // Separate filename and extension.
        int dotIndex = originalName.lastIndexOf('.');

        String nomFichier = originalName;
        String extensionFichier = "";

        if (dotIndex > 0) {
            nomFichier = originalName.substring(0, dotIndex);
            extensionFichier = originalName.substring(dotIndex + 1);
        }

        // Read file content into byte[].
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream input = part.getInputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }

        byte[] contenuFichier = output.toByteArray();

        MapFile file = new MapFile(
            nomFichier,
            extensionFichier,
            contenuFichier
        );

        // Save the uploaded file.
        saveMapFile(file);

        return file;
    }
    void saveMapFile(MapFile file) throws IOException {

        String directory = getServletContext()
            .getInitParameter("upload.directory");

        if (directory == null || directory.trim().isEmpty()) {
            throw new IOException(
                "Upload directory is not configured"
            );
        }

        Path uploadDirectory = Paths.get(directory);

        if (!uploadDirectory.isAbsolute()) {
            throw new IOException(
                "Upload directory must be an absolute path"
            );
        }

        Files.createDirectories(uploadDirectory);

        // Only accept simple filename extensions.
        String extension = file.getExtensionFichier()
            .toLowerCase(java.util.Locale.ROOT);

        if (!extension.matches("[a-z0-9]{0,15}")) {
            throw new IOException("Invalid file extension");
        }

        // Generate a unique filename to avoid collisions
        // and avoid trusting the client-supplied filename.
        String savedName = UUID.randomUUID().toString();

        if (!extension.isEmpty()) {
            savedName += "." + extension;
        }

        Path destination = uploadDirectory.resolve(savedName);

        Files.write(
            destination,
            file.getContenuFichier(),
            StandardOpenOption.CREATE_NEW
        );

        System.out.println("File saved to: " + destination);
    }

}