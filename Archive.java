import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import uchi.utils.MethodEnum;
import uchi.utils.UrlMethod;

public class Archive {
    public static void main(String[] args) {
        JSONObject obj = new JSONObject();
        PrintWriter out = response.getWriter();

        String contextPath = request.getContextPath();
        // String url = request.getRequestURL().toString();
        String route = request.getPathInfo();
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
        } else {
            obj.put("status", 404);
            obj.put("error", "The route " + route +" you're trying to get to doesn't exist");
        }
        out.print(obj);
    }
}
