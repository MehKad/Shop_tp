package pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Products;

public class Home extends HttpServlet {
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();

      HttpSession session = request.getSession(false);
      if (session != null) {
         String username = (String) session.getAttribute("username");

         @SuppressWarnings("unchecked")
         List<Products> pros = (List<Products>) session.getAttribute("products");

         if (username == null || pros == null) {
            response.sendRedirect("./");
         } else {
            out.println("<html><head><title>Home</title></head><body>");
            out.println("<h1>Bienvenu " + username + ", dans la servlet Achat</h1>");
            out.println("<p>(Liste des produit pour achat)</p>");
            out.println("<table border='1'>");
            out.println("<tr><th>Name</th><th>Description</th><th>Price</th><th>Action</th></tr>");
            for (Products i : pros) {
               out.println("<tr>");
               out.println("<td>" + i.getName() + "</td>");
               out.println("<td>" + i.getDescription() + "</td>");
               out.println("<td>" + i.getPrice() + "</td>");
               out.println("<td>Add to cart</td>");
               out.println("</tr>");
            }
            out.println("</table></body></html>");
         }
      } else {
         response.sendRedirect("./");
      }
   }
}
