package servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class registerServlet extends HttpServlet {

   protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      String username = request.getParameter("nom");
      String password = request.getParameter("password");

      // Create a cookie
      String userCredentials = username + ":" + password;
      Cookie userCookie = new Cookie("user", userCredentials);
      userCookie.setMaxAge(600); // 10 minutes (in seconds)
      response.addCookie(userCookie);

      // Redirect to login page
      response.sendRedirect("./Login");
   }

}
