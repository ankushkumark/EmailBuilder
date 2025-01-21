package servlets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/getEmailLayout")
public class GetEmailLayoutServlet extends HttpServlet {
    private static final String LAYOUT_PATH = "/path/to/layout.html"; // Change this to actual path of layout.html

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        try {
            String layout = new String(Files.readAllBytes(Paths.get(LAYOUT_PATH)));
            response.getWriter().write(layout);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error loading email layout.");
        }
    }
}
