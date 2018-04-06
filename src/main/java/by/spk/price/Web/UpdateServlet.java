package by.spk.price.Web;

import by.spk.price.GetCSV.GetCSV;
import by.spk.price.PutCSV.PutCSV;
import by.spk.price.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/update")
public class UpdateServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            GetCSV.get();
            PutCSV.put();
        } catch (Exception e) {
            resp.setStatus(500);
            throw new IllegalStateException("Error Update Data: " + e.getMessage());
        }

        resp.sendRedirect(Utils.getPropertiesValue("web_url_path") + "/price?updated=ok");
    }
}