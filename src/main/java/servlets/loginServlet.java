package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class loginServlet extends HttpServlet {
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {

      String enteredUsername = request.getParameter("nom");
      String enteredPassword = request.getParameter("password");
      String cookieUsername = request.getParameter("cookieUsername");
      String cookiePassword = request.getParameter("cookiePassword");

      boolean isAuthenticated = false;
      if (enteredUsername != null && enteredPassword != null &&
            enteredUsername.equals(cookieUsername) && enteredPassword.equals(cookiePassword)) {
         isAuthenticated = true;
      }

      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      out.println("<html><head><title>Login Result</title></head><body>");
      if (isAuthenticated) {
         out.println("<h1>Login Successful</h1>");
         out.println("<p>Welcome back, " + enteredUsername + "!</p>");
      } else {
         out.println("<h1>Login Failed</h1>");
         out.println("<p>Invalid username or password.</p>");
      }
      out.println("</body></html>");
   }
}
