package by.spk.price.web.filter;

import by.spk.price.Utils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AbstractFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        boolean isLogged = false;

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Utils.getPropertiesValue("web_cookie_key")) &&
                        cookie.getValue().equals(Utils.getPropertiesValue("web_cookie_value"))) {
                    isLogged = true;
                    break;
                }
            }
        }

        if (isLogged) {
            chain.doFilter(req, res);
        } else {
            res.setStatus(401);
            res.getWriter().println("Forbidden");
            res.sendRedirect(Utils.getPropertiesValue("web_url_path") + "/login");
        }
    }
}
