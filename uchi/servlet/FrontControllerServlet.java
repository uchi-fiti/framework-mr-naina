package uchi.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import uchi.exceptions.DuplicateRouteException;
import uchi.exceptions.NoSuchRouteException;
import uchi.utils.Mapping;
import uchi.utils.MethodEnum;
import uchi.utils.UScanner;
import uchi.utils.UrlMethod;
import org.json.JSONObject;

public class FrontControllerServlet extends HttpServlet {
    private List <String> controllersName = new ArrayList<>();
    private Map <UrlMethod, Mapping> mappings = new HashMap<>();
    private Exception exception = null;
    @Override
    // TODO: raha efa misy ilay route de mi-throw exception
    public void init() {
        controllersName = (List<String>) getServletContext().getAttribute("controllersName");
        mappings = (Map<UrlMethod, Mapping>) getServletContext().getAttribute("mappings");
        exception = (Exception) getServletContext().getAttribute("exception");
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject obj = new JSONObject();
        PrintWriter out = response.getWriter();

        String contextPath = request.getContextPath();
        // String url = request.getRequestURL().toString();
        String route = request.getRequestURI().replace(contextPath, "");
        MethodEnum methodEnum = MethodEnum.POST;
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if(mappings.containsKey(new UrlMethod(route, methodEnum))) {
            Mapping mapping = mappings.get(new UrlMethod(route, methodEnum));
            obj.put("response", "POST route exists !");
            obj.put("route", route);
            Map <String, Object> mapFunction = new HashMap<>();
            mapFunction.put("nom", mapping.getMethod().getName());
            mapFunction.put("typeDeRetour", mapping.getMethod().getReturnType().getName());
            mapFunction.put("nombreDeParametres", String.valueOf(mapping.getMethod().getParameterCount()));
            mapFunction.put("typeDesParametres", mapping.getMethod().getParameterTypes());
            obj.put("fonction", mapFunction);
            obj.put("controller", mapping.getController().getName());
            out.print(obj);
        }
        obj.put("response", "POST route doesn't exist");
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
        String contextPath = request.getContextPath();
        String url = request.getRequestURL().toString();
        PrintWriter out = response.getWriter();
        out.println("L'url complet est: " + url);
        if(controllersName.size() > 0) {
            out.println("Nom complet des controllers: ");
            for (String name : controllersName) {
                out.println(name);
            }
        }
        String methodStr = request.getMethod();
        MethodEnum methodEnum = methodStr.equalsIgnoreCase("get") ? MethodEnum.GET : MethodEnum.POST;
        String route = request.getRequestURI().replace(contextPath, "");
        Mapping m = mappings.getOrDefault(new UrlMethod(route, methodEnum), null);
        if (m != null) {
            out.println("La route existe !");
            out.println("Route: " + route + ", Fonction: " + m.getMethod().getName() + ", Controller: " + m.getController().getName());
            Class <?> returnType = m.getMethod().getReturnType();
            Class <?>[] parameterTypes = m.getMethod().getParameterTypes();
            Object objectInstance = m.getController().getDeclaredConstructor().newInstance();
            if (parameterTypes.length == 0) {
                if(returnType == void.class) {
                    m.getMethod().invoke(objectInstance);
                    out.println("Function with void return type called !");
                } else {
                    Object obj = m.getMethod().invoke(objectInstance);
                    out.println("Function called ! Returned : " + obj.toString());
                }
            } // else ... (tantara hafa mihitsy satria ilay route lo tsy maintsy misy parametre vao tokony hisy parametre ilay fonction ho antsoina)

            out.println("Arrived here !");
        } else {
            throw new NoSuchRouteException(route, methodEnum, mappings);
        }
        out.flush();
    }
}