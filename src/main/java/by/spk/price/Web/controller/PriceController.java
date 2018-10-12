package by.spk.price.Web.controller;

import by.spk.price.Web.WebDAO;
import by.spk.price.entity.WebPrice;
import by.spk.price.Utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/price")
public class PriceController extends HttpServlet {

    private WebDAO dao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*int limit = 1000;
        int offset = 0;

        String str = req.getParameter("limit");
        if (str != null && !str.isEmpty()) {
            try {
                limit = Integer.parseInt(str.trim());
            } catch (NumberFormatException e) {
                limit = 0;
            }
        }
        str = req.getParameter("offset");
        if (str != null && !str.isEmpty()) {
            try {
                offset = Integer.parseInt(str.trim());
            } catch (NumberFormatException e) {
                offset = 0;
            }
        }
        */

        Boolean updated = false;

        String str = req.getParameter("updated");
        if (str != null && !str.isEmpty()) {
            updated = true;
        }

        String product = req.getParameter("product");
        if (product != null && !product.isEmpty()) {
            //List<WebPrice> prices = dao.show(offset, limit, product.trim());
            List<WebPrice> prices = dao.show(product.trim());
            req.setAttribute("prices", prices);
            req.setAttribute("product", product);
        }
        req.setAttribute("updated", updated);
        //if (limit > 0) req.setAttribute("limit", limit);

        //

        req.setAttribute("urlDomain", Utils.getPropertiesValue("web_url_domain"));
        req.setAttribute("urlSearch", Utils.getPropertiesValue("web_url_search"));
        req.setAttribute("title", "price");
        req.setAttribute("dateUpdated", dao.getValue("updated"));
        req.setAttribute("ver", Utils.getVersion());

        req.getServletContext().getRequestDispatcher("/WEB-INF/price.jsp").forward(req, resp);
    }

    @Override
    public void init() {
        dao = Utils.getDAO();
    }
}

