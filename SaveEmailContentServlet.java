package servlets;

import utils.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/saveEmailContent")
public class SaveEmailContentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Form se data retrieve karo
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String footer = request.getParameter("footer");
        String imageUrl = request.getParameter("imageUrl");

        // Database connection setup
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO email_content (title, content, footer, image_url) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, content);
            preparedStatement.setString(3, footer);
            preparedStatement.setString(4, imageUrl);

            int rows = preparedStatement.executeUpdate();
            if (rows > 0) {
                response.getWriter().write("Email content saved successfully!");
            } else {
                response.getWriter().write("Failed to save email content.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}
