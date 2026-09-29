package uchi.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import uchi.exceptions.DuplicateRouteException;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
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
        ServletContext context = sce.getServletContext();
        context.setAttribute("controllersName", controllersName);
        context.setAttribute("mappings", mappings);
        context.setAttribute("exception", exception);
        // Code here runs exactly ONCE when the web app starts up
        System.out.println("-----------------------------------");
        System.out.println("L'application web est en train de démarrer...");
        System.out.println("-----------------------------------");
        
        System.out.println("???????????????????????");
        System.out.println("Is school found??");
        System.out.println("???????????????????????");

        WebApplicationContext springContext =
        WebApplicationContextUtils.getWebApplicationContext(
            sce.getServletContext()
        );
    try {
        Object school = springContext.getBean("school");

        System.out.println("Bean trouvé : " + school);
        System.out.println("Classe : " + school.getClass().getName());

        Method getName = school.getClass().getMethod("getName");
        Method getId = school.getClass().getMethod("getId");

        Object name = getName.invoke(school);
        Object id = getId.invoke(school);

        System.out.println("School name : " + name);
        System.out.println("School id   : " + id);

    } catch (Exception e) {
        System.err.println("Erreur lors de la récupération du bean 'school' :");
        e.printStackTrace();
    }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Code here runs exactly ONCE when the web app shuts down
        System.out.println("Web application is shutting down...");
    }
    
}
