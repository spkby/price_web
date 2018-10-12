package by.spk.price.web.filter;

import by.spk.price.Utils;

import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter("/")
public class RootFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException {
        res.sendRedirect(Utils.getPropertiesValue("web_url_path") + "/price");
    }
}
