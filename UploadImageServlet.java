package servlets;

import utils.DatabaseConnection;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;


@WebServlet("/uploadImage")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class UploadImageServlet extends HttpServlet {
    private static final String SAVE_DIR = "uploaded_images";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Create directory to save uploaded images if it doesn't exist
        String uploadPath = getServletContext().getRealPath("") + File.separator + SAVE_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            // Get the uploaded file
            Part filePart = request.getPart("file");
            String fileName = filePart.getSubmittedFileName();

            // Save file to the directory
            String filePath = uploadPath + File.separator + fileName;
            filePart.write(filePath);

            // Save file path in the database
            Connection connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO uploaded_images (file_name, file_path) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, fileName);
            statement.setString(2, filePath);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                out.println("Image uploaded and saved successfully!");
            } else {
                out.println("Failed to save image details in the database.");
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error: " + e.getMessage());
        }
    }
}
