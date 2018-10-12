package by.spk.price.Web.controller;

import by.spk.price.Web.WebDAO;
import by.spk.price.entity.WebProduct;
import by.spk.price.Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@WebServlet("/save")
public class SavePricesController extends HttpServlet {

    private WebDAO dao;

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
      /*  res.getWriter().println("start<br>");

        for (Map.Entry<String, String[]> entry : req.getParameterMap().entrySet()) {
            res.getWriter().println(entry.getKey() + " - " + entry.getValue()[0] + "<br>");
        }*/

            List<WebProduct> products = new ArrayList<>();
            String[] values = new String[req.getParameterMap().size()];

            int i = 0;
            for (Map.Entry<String, String[]> entry : req.getParameterMap().entrySet()) {
                values[i++] = entry.getValue()[0];
            }

        /*for (String s:values){
            res.getWriter().println(s + "<br>");
        }*/

            for (i = 0; i < values.length / 3; i++) {
                int id = Integer.parseInt(values[0 + i * 3]);
                double lastPrice = Double.parseDouble(values[1 + i * 3]);
                double percent = Double.parseDouble(values[2 + i * 3]);
                products.add(new WebProduct(id, lastPrice, percent));
            }

       /* for (WebProduct prod: products){
            res.getWriter().println(prod.getId() +" "+ prod.getLastPrice() + " "+ prod.getPercent() + "<br>");
        }*/

            dao.savePrices(products);


        } catch (Exception e) {
            res.setStatus(500);
            throw new IllegalStateException("Error Set New Products: " + e.getMessage());
        }

        res.sendRedirect(Utils.getPropertiesValue("web_url_path") + "/price");
    }

    @Override
    public void init() throws ServletException {
        dao = Utils.getDAO();
    }
}
