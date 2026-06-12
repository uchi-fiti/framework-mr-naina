package uchi.servlet;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontControllerServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = request.getRequestURL().toString();
        PrintWriter out = response.getWriter();
        out.print("Hello world ! Full url: " + url);
    }
}