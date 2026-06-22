package uchi.exceptions;

import java.util.Map;

import uchi.utils.Mapping;

public class NoSuchRouteException extends Exception {
    private static String getErrorMessage(String route, Map <String, Mapping> mappings) {
        StringBuilder sb = new StringBuilder("La route " + route + " n'existe pas, mais voici les routes disponibles: \n");
        for(Map.Entry<String, Mapping> entry : mappings.entrySet()) {
            sb.append("Route: " + entry.getKey() + " Fonction: " + entry.getValue().getMethod().getName() + " Controller: " + entry.getValue().getController().getName() + "\n");
        }
        return sb.toString();
    }
    public NoSuchRouteException(String route, Map <String, Mapping> mappings) {
        super(getErrorMessage(route, mappings));
    }
}
