package servlets;

import utils.DatabaseConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/renderAndDownloadTemplate")
public class RenderAndDownloadTemplateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id"); // Email ID to fetch from database
        System.out.println("Received ID: " + id); // Debugging ID

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM email_content WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            System.out.println("Executing query: SELECT * FROM email_content WHERE id = " + id); // Debug SQL

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Fetching data from database
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                String footer = resultSet.getString("footer");
                String imageUrl = resultSet.getString("image_url");

                System.out.println("Fetched data: " + title + ", " + content + ", " + footer + ", " + imageUrl); // Debug data

                // Creating final HTML content
                String emailTemplate = """
                        <html>
                        <head><title>Email Template</title></head>
                        <body>
                            <h1>%s</h1>
                            <p>%s</p>
                            <img src="%s" alt="Email Image" style="max-width:600px;"/>
                            <footer>%s</footer>
                        </body>
                        </html>
                        """.formatted(title, content, imageUrl, footer);

                // Setting response headers for file download
                response.setContentType("text/html");
                response.setHeader("Content-Disposition", "attachment; filename=email_template.html");

                PrintWriter out = response.getWriter();
                out.write(emailTemplate);
                out.close();
            } else {
                System.out.println("No data found for ID: " + id); // Debug no data
                response.getWriter().write("No email content found with the given ID.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}
