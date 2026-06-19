package uchi.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import uchi.annotations.UController;

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
            Class<? extends java.lang.annotation.Annotation> ann = (Class<? extends java.lang.annotation.Annotation>) annotationClass;
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
}
