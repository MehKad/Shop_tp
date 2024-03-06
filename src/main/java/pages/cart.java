package pages;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import models.Cart;

public class cart extends HttpServlet {

   protected void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();

      HttpSession session = request.getSession(false);
      if (session != null) {
         String username = (String) session.getAttribute("username");

         String cartSessionAttributeName = "cart_" + username;

         @SuppressWarnings("unchecked")
         List<Cart> carts = (List<Cart>) session.getAttribute(cartSessionAttributeName);

         out.println("<html><head><title>Cart page</title></head><body>");
         out.println("<h1>Bienvenue " + username + ", dans la servlet Achat</h1><br>");
         out.println("<h2>(Votre panier)</h2>");
         out.println("<table border='1'>");
         out.println("<tr><th>Name</th><th>Price</th></tr>");
         double totalPrice = 0;
         int numberOfItems = 0;
         if (carts != null) {
            for (Cart item : carts) {
               out.println("<tr>");
               out.println("<td>" + item.getName() + "</td>");
               out.println("<td>" + item.getPrice() + "</td>");
               out.println("</tr>");
               totalPrice += item.getPrice();
               numberOfItems++;
            }
         } else {
            out.println("<tr><td colspan='2'>Votre panier est vide</td></tr>");
         }
         out.println("</table>");
         out.println("<p>Votre panier contient : " + numberOfItems + " produit</p>");
         out.println("<a href=\"./Home\">Vous pouvez commander un autre produit</a><br>");
         out.println("<a href=\"./save\">Vous pouvez enregistrer votre commande</a>");
         out.println("</body></html>");
      } else {
         response.sendRedirect("./");
      }
   }
}