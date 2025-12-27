package com.example.demo.servlet;

import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;

public class HelloServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain");
        resp.getWriter().write("Hello from Dynamic Event Ticket Pricing Servlet");
    }
}
