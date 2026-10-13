package uchi.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
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
    Object [] resolveMethodParameterValues(HttpServletRequest request, Method method) throws IllegalParameterException {
        Parameter[] parameters = method.getParameters();
        Object [] values = new Object[parameters.length];
        int i = 0;
        for(Parameter p : parameters) {
            values[i] = convertParameterValue(request.getParameter(p.getName()), p.getType());
            i++;
        }
        return values;
    }
    Object convertParameterValue(String value, Class <?> type) throws IllegalParameterException {
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
        throw new IllegalParameterException("Type de paramètre non encore prise en charge: " + type.getName());
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


}