package uchi.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import uchi.annotations.UController;
import uchi.annotations.UrlMapping;

public class UScanner {
    public static List<Class<?>> getClasses(String packageName) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL resource = loader.getResource(path);
        if (resource == null) {
            return classes;
        }
        File directory = new File(resource.toURI());
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".class")) {
                        String className = packageName + "." + file.getName().replace(".class", "");
                        classes.add(Class.forName(className));
                    }
                }
            }
        }
        return classes;
    }

    public static List<Class<?>> getClassesWithAnnotation(List<Class<?>> classes, Class<?> annotationClass) {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        if (annotationClass.isAnnotation()) {
            Class<? extends Annotation> ann = (Class<? extends Annotation>) annotationClass;
            for (Class<?> clazz : classes) {
                if (clazz.isAnnotationPresent(ann)) {
                    annotatedClasses.add(clazz);
                }
            }
        }
        return annotatedClasses;
    }

    public static List<String> getControllers(String packageName) throws Exception {
        List<Class<?>> classes = getClasses(packageName);
        List<Class<?>> controllersClasses = getClassesWithAnnotation(classes, UController.class);
        List<String> controllers = new ArrayList<>();
        for (Class<?> clazz : controllersClasses) {
            controllers.add(clazz.getName());
        }
        return controllers;
    }
    public static Map<UrlMethod, Mapping> getUrlMappings(Class<?> clazz) {

        Map<UrlMethod, Mapping> routes = new HashMap<>();

        for (Method method : clazz.getDeclaredMethods()) {

            if (method.isAnnotationPresent(UrlMapping.class)) {

                UrlMapping annotation = method.getAnnotation(UrlMapping.class);

                String url = annotation.url();
                MethodEnum m = annotation.method();
                // System.out.println("------------------------------------------");
                // System.out.println("Url: " + url);
                // System.out.println("------------------------------------------");
                routes.put(new UrlMethod(url, m), new Mapping(clazz, method));
            }
        }

        return routes;
}
}
