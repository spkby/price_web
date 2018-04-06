package by.spk.price.Web;

import by.spk.price.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setAttribute("ver", Utils.getVersion());
        req.setAttribute("title", "login");

        req.getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String name = req.getParameter("login");
        String password = req.getParameter("password");

        if (name.equals(Utils.getPropertiesValue("web_login"))
                && password.equals(Utils.getPropertiesValue("web_password"))) {
            resp.addCookie(new Cookie(Utils.getPropertiesValue("web_cookie_key"),
                    Utils.getPropertiesValue("web_cookie_value")));
            resp.sendRedirect(Utils.getPropertiesValue("web_url_path") + "/");
        } else {
            resp.setStatus(500);
            req.setAttribute("error", true);
            req.getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
        }
    }
}