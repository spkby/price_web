package by.spk.price.web.filter;

import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter("/")
public class RootFilter extends HttpFilter {

    @Override
    protected void doFilter(final HttpServletRequest req, final HttpServletResponse res, final FilterChain chain)
            throws IOException {
        res.sendRedirect(getServletContext().getContextPath() + "/price");
    }
}
