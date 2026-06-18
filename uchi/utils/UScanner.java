package uchi.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import uchi.annotations.UController;

public class UScanner {
    public static List<String> getControllers() throws Exception {

        List<String> controllers = new ArrayList<>();

        String packageName = "controllers";

        String path = packageName.replace('.', '/');

        ClassLoader loader =
                Thread.currentThread().getContextClassLoader();

        URL resource = loader.getResource(path);

        File directory = new File(resource.toURI());

        for (File file : directory.listFiles()) {

            if (!file.getName().endsWith(".class")) {
                continue;
            }

            String className =
                    packageName + "."
                    + file.getName().replace(".class", "");

            Class<?> clazz =
                    Class.forName(className);

            if (clazz.isAnnotationPresent(UController.class)) {
                controllers.add(clazz.getName());
            }
        }

        return controllers;
    }
}
