package uchi.exceptions;

import java.lang.reflect.Method;
import java.util.Map;

import uchi.utils.Mapping;
import uchi.utils.MethodEnum;
import uchi.utils.UrlMethod;

public class NoSuchRouteException extends Exception {
    // private static String getErrorMessage(String route, Map <String, Mapping> mappings) {
    //     StringBuilder sb = new StringBuilder("La route " + route + " n'existe pas, mais voici les routes disponibles: \n");
    //     for(Map.Entry<String, Mapping> entry : mappings.entrySet()) {
    //         sb.append("Route: " + entry.getKey() + " Fonction: " + entry.getValue().getMethod().getName() + " Controller: " + entry.getValue().getController().getName() + "\n");
    //     }
    //     return sb.toString();
    // }
    // public NoSuchRouteException(String route, Map <String, Mapping> mappings) {
        //     super(getErrorMessage(route, mappings));
        // }
        private static String getUrlMethodErrorMessage(String route, MethodEnum methodEnum, Map <UrlMethod, Mapping> mappings) {
            StringBuilder sb = new StringBuilder("La route " + methodEnum + " " + route + " n'existe pas, mais voici les routes disponibles: \n");
            for(Map.Entry<UrlMethod, Mapping> entry : mappings.entrySet()) {
                sb.append("Route: " + entry.getKey().getUrl() + " [method " + entry.getKey().getMethod() + "] Fonction: " + entry.getValue().getMethod().getName() + " Controller: " + entry.getValue().getController().getName() + "\n");
            }
            return sb.toString();
        }
    public NoSuchRouteException(String route, MethodEnum methodEnum, Map <UrlMethod, Mapping> mappings) {
        super(getUrlMethodErrorMessage(route, methodEnum, mappings));
    }
}
