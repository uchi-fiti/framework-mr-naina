package uchi.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import uchi.utils.UScanner;

public class FrontControllerServlet extends HttpServlet {
    private List <String> controllersName;
    @Override
    public void init() {
        try {
            controllersName = UScanner.getControllers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = request.getRequestURL().toString();
        PrintWriter out = response.getWriter();
        out.println("Hello world ! Full url: " + url);
        if(controllersName.size() > 0) {
            out.println("Nom complet des controllers: ");
            for (String name : controllersName) {
                out.println(name);
            }
        }
    }
}