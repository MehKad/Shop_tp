package pages;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Login extends HttpServlet {
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();

      // Retrieve the cookie
      Cookie[] cookies = request.getCookies();
      String username = null;
      String password = null;
      if (cookies != null) {
         for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user")) {
               String[] credentials = cookie.getValue().split(":");
               if (credentials.length == 2) {
                  username = credentials[0];
                  password = credentials[1];
               }
            }
         }
      }

      // Display username and password
      out.println("<html><head><title>Login</title></head><body>");
      out.println("<p>Parametre recu : NR = null MPR= null NC = " + username + " MPC = " + password + "</p>");
      out.println("<h1>Authentification</h1><br>");
      out.println("<p>(Le nom et le mot de passe > 3 caractères !!!)</p>");
      out.println("<form action=\"loginServlet\" method=\"post\">");

      out.println("<input type=\"hidden\" name=\"cookieUsername\" value=\"" + username + "\">");
      out.println("<input type=\"hidden\" name=\"cookiePassword\" value=\"" + password + "\">");
      out.println("<label for=\"nom\">nom</label>");
      out.println("<input type=\"text\" name=\"nom\">");
      out.println("<label for=\"password\">password</label>");
      out.println("<input type=\"password\" name=\"password\">");
      out.println("<input type=\"submit\" value=\"Sidentifier\">");
      out.println("</form>");
      out.println("</body></html>");
   }
}
