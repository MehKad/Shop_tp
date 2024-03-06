package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import models.Cart;
import configs.DBConfig;

public class saveServlet extends HttpServlet {
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

         if (carts != null && !carts.isEmpty()) {

            double totalPrice = 0.0;
            for (Cart item : carts) {
               totalPrice += item.getPrice();
            }
            try (Connection conn = DriverManager.getConnection(DBConfig.getInstance().getJdbcUrl(),
                  DBConfig.getInstance().getDbUser(), DBConfig.getInstance().getDbPassword())) {

               String insertCommandeSql = "INSERT INTO commande (username, total_price) VALUES (?, ?)";
               try (PreparedStatement pstmt = conn.prepareStatement(insertCommandeSql,
                     Statement.RETURN_GENERATED_KEYS)) {
                  pstmt.setString(1, username);
                  pstmt.setDouble(2, totalPrice);
                  pstmt.executeUpdate();

                  ResultSet generatedKeys = pstmt.getGeneratedKeys();
                  int commandeId = -1;
                  if (generatedKeys.next()) {
                     commandeId = generatedKeys.getInt(1);
                  }

                  String insertCartItemSql = "INSERT INTO cart_items (commande_id, product_name, price) VALUES (?, ?, ?)";
                  try (PreparedStatement cartItemStmt = conn.prepareStatement(insertCartItemSql)) {
                     for (Cart i : carts) {
                        cartItemStmt.setInt(1, commandeId);
                        cartItemStmt.setString(2, i.getName());
                        cartItemStmt.setDouble(3, i.getPrice());
                        cartItemStmt.executeUpdate();
                     }
                  }
               }
            } catch (SQLException e) {
               e.printStackTrace();
            }

            out.println("<html><head><title>Cart Summary</title></head><body>");
            out.println("<h1>bienvenue " + username + ", dans la servlet Achat</h1>");
            out.println("<p>Voici ta commande complète</p>");
            out.println("<table border='1'>");
            out.println("<tr><th>Name</th><th>Price</th></tr>");
            for (Cart i : carts) {
               out.println("<tr>");
               out.println("<td>" + i.getName() + "</td>");
               out.println("<td>" + i.getPrice() + "</td>");
               out.println("</tr>");
            }
            out.println("</table>");
            out.println("<h5>Total Price: " + totalPrice + "$</h5>");
            out.println("<a href='./'>Effectuer un autre achat</a>");
         } else {
            out.println("<h3>Votre panier est vide</h3>");
         }
      } else {
         response.sendRedirect("./");
      }

   }
}
