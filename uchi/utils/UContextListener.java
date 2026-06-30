package uchi.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import uchi.exceptions.DuplicateRouteException;
@WebListener
public class UContextListener implements ServletContextListener {
    private List <String> controllersName = new ArrayList<>();
    private Map <UrlMethod, Mapping> mappings = new HashMap<>();
    private Exception exception = null;

    public void init() {
        String packageName = "controllers";
        try {
            controllersName = UScanner.getControllers(packageName);

            for (String className : controllersName) {

                Class<?> clazz = Class.forName(className);
                for (Map.Entry<UrlMethod, Mapping> entry:  UScanner.getUrlMappings(clazz).entrySet()) {
                    if(!mappings.containsKey(entry.getKey())) {
                        mappings.put(entry.getKey(), entry.getValue());
                    } else {
                        throw new DuplicateRouteException(entry.getKey(), mappings.get(entry.getKey()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
        }
    }
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        init();
        sce.getServletContext().setAttribute("controllersName", controllersName);
        sce.getServletContext().setAttribute("mappings", mappings);
        sce.getServletContext().setAttribute("exception", exception);
        // Code here runs exactly ONCE when the web app starts up
        System.out.println("-----------------------------------");
        System.out.println("Web application is starting up...");
        System.out.println("-----------------------------------");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Code here runs exactly ONCE when the web app shuts down
        System.out.println("Web application is shutting down...");
    }
    
}
