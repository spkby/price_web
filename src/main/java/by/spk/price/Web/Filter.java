package by.spk.price.Web;

import by.spk.price.Utils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@WebFilter("/*")
public class Filter extends HttpFilter {

    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("", "/", "/login")));


    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        String path = req.getRequestURI().substring(req.getContextPath().length()).replaceAll("[/]+$", "");
        boolean allowedPath = ALLOWED_PATHS.contains(path);

        boolean isLogged = false;

        boolean allowedExt = req.getRequestURI().matches(".*(css|jpg|png|gif|js)");


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

        if (isLogged || allowedPath || allowedExt) {
            chain.doFilter(req, res);
        } else {
            res.setStatus(401);
            res.getWriter().println("Forbidden");
            res.sendRedirect(Utils.getPropertiesValue("web_url_path") + "/login");
        }
    }
}
