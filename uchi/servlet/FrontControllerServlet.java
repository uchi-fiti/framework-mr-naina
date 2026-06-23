package uchi.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import uchi.exceptions.NoSuchRouteException;
import uchi.utils.Mapping;
import uchi.utils.UScanner;

public class FrontControllerServlet extends HttpServlet {
    private List <String> controllersName;
    private Map <String, Mapping> mappings = new HashMap<>();
    @Override
    public void init() {
        String packageName = "controllers";
        try {
            controllersName = UScanner.getControllers(packageName);

        for (String className : controllersName) {

            Class<?> clazz =
                    Class.forName(className);

            mappings.putAll(
                    UScanner.getUrlMappings(clazz)
            );
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            processRequest(request, response);
        } catch (NoSuchRouteException e) {
            PrintWriter err = response.getWriter();
            err.print(e);
        }
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            processRequest(request, response);
        } catch (NoSuchRouteException e) {
            PrintWriter err = response.getWriter();
            err.print(e);
        }
    }
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, NoSuchRouteException{
        String contextPath = request.getContextPath();
        String url = request.getRequestURL().toString();
        PrintWriter out = response.getWriter();
        out.println("Hello world ! Full url lol: " + url);
        if(controllersName.size() > 0) {
            out.println("Nom complet des controllers: ");
            for (String name : controllersName) {
                out.println(name);
            }
        }
        String route = request.getRequestURI().replace(contextPath, "");
        out.println("Route: "+ route);
        Mapping m = mappings.getOrDefault(route, null);
        if (m != null) {
             out.println("Route: " + route + ", Fonction: " + m.getMethod().getName() + ", Controller: " + m.getController().getName());
        } else {
            throw new NoSuchRouteException(route, mappings);
        }
    }
}