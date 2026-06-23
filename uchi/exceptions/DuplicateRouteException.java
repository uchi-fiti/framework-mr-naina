package uchi.exceptions;

import uchi.utils.Mapping;
import uchi.utils.UrlMethod;

public class DuplicateRouteException extends Exception {
    // public static String getErrorMessage(String url, Mapping mapping) {
    //     return "La route [" + url + "] existe deja. Controller: [" + mapping.getController().getName() + "]. Methode: [" + mapping.getMethod().getName() + "]. ";
    // }
    // public DuplicateRouteException(String url, Mapping mapping) {
    //     super(getErrorMessage(url, mapping));
    // }
    public static String getErrorMessage(UrlMethod um, Mapping mapping) {
        return "La route [" + um.getUrl() + "] existe deja.(method"+ um.getMethod() + ") Controller: [" + mapping.getController().getName() + "]. Methode: [" + mapping.getMethod().getName() + "]. ";
    }
    public DuplicateRouteException(UrlMethod um, Mapping mapping) {
        super(getErrorMessage(um, mapping));
    }
}
