package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import models.Products;
import configs.DBConfig;

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

      if (isAuthenticated) {

         HttpSession session = request.getSession(true);
         session.setAttribute("username", enteredUsername);

         try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DBConfig.getInstance().getJdbcUrl(),
                  DBConfig.getInstance().getDbUser(),
                  DBConfig.getInstance().getDbPassword())) {
               List<Products> productList = new ArrayList<>();
               String itemSql = "SELECT name, description, price FROM products";
               try (PreparedStatement proStatement = conn.prepareStatement(itemSql)) {
                  try (ResultSet proResultSet = proStatement.executeQuery()) {
                     while (proResultSet.next()) {
                        Products pro = new Products();
                        pro.setName(proResultSet.getString("name"));
                        pro.setDescription(proResultSet.getString("description"));
                        pro.setPrice(proResultSet.getDouble("price"));
                        productList.add(pro);
                     }
                  }
               }

               session.setAttribute("products", productList);
               response.sendRedirect("./Home");

            }
         } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
         }

      } else {
         response.sendRedirect("./Login");
      }
   }
}
