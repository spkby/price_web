package by.spk.price.web.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/error/*")
public class ErrorController extends HttpServlet {

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        final Long code = parseLong(req);
        req.getServletContext().getRequestDispatcher("/WEB-INF/errors/" + code + ".jsp").forward(req, resp);
    }

    private Long parseLong(final HttpServletRequest req) {
        long id;

        if (req.getPathInfo() == null) {
            throw new IllegalArgumentException("empty");
        }
        String[] strings = req.getPathInfo().split("/");

        if (strings.length > 1) {
            try {
                id = Long.parseLong(strings[strings.length - 1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            throw new IllegalArgumentException("empty");
        }
        return id;
    }
}