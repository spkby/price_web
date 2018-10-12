package by.spk.price.web;

import by.spk.price.entity.WebProduct;
import by.spk.price.entity.WebPrice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WebDAO {

    private static final String PRICE = "select prices.id as id, subcategory, item, product," +
            " recommendedprice, lastprice, percent, product_id from prices" +
            " inner join products on prices.product_id = products.id" +
            " inner join subcategories on prices.subcategory_id = subcategories.id" +
            " OFFSET ? LIMIT ?;";

    /*private static final String PRICE_WHERE = "select prices.id as id, subcategory, item, product," +
            " recommendedprice, lastprice, percent, product_id from prices" +
            " inner join products on prices.product_id = products.id" +
            " inner join subcategories on prices.subcategory_id = subcategories.id" +
            " where LOWER(product) like LOWER(?)" +
            " OFFSET ? LIMIT ?;";*/

    private static final String PRICE_WHERE = "select prices.id as id, subcategory, item, product," +
            " recommendedprice, lastprice, percent, product_id from prices" +
            " inner join products on prices.product_id = products.id" +
            " inner join subcategories on prices.subcategory_id = subcategories.id" +
            " where LOWER(product) like LOWER(?);";

    private static final String UPDATE_PRICE = "update products set lastprice = ?, percent = ? where id = ?;";
    private static final String SELECT_VALUE = "select val from data where key = ?;";

    private PreparedStatement priceStatement;
    private PreparedStatement priceWhereStatement;
    private PreparedStatement updatePriceStatement;
    private PreparedStatement selectValueStatement;

    public WebDAO(Connection connection) {
        try {
            priceStatement = connection.prepareStatement(PRICE);
            priceWhereStatement = connection.prepareStatement(PRICE_WHERE);
            updatePriceStatement = connection.prepareStatement(UPDATE_PRICE);
            selectValueStatement = connection.prepareStatement(SELECT_VALUE);

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error");
        }
    }

    public void savePrices(List<WebProduct> products) {

        try {
            for (WebProduct prod : products) {
                updatePriceStatement.setDouble(1, prod.getLastPrice());
                updatePriceStatement.setInt(3, prod.getId());
                updatePriceStatement.setDouble(2, prod.getPercent());
                updatePriceStatement.addBatch();
            }
            updatePriceStatement.executeBatch();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error set new prices: " + e.getMessage());
        }
    }

    public String getValue(String key) {
        String value = "";
        try {
            selectValueStatement.setString(1, key);
            selectValueStatement.execute();
            ResultSet rows = selectValueStatement.getResultSet();
            while (rows.next()) {
                value = rows.getString(1);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error get Value by Key(" + key + "): " + e.getMessage());
        }
        return value;
    }

    /*public List<WebPrice> show(int offset, int limit, String where) {
        List<WebPrice> prices = new ArrayList<>();
        try {
            ResultSet rows;
            if (where == null || where.isEmpty()) {
                priceStatement.setInt(1, offset);
                priceStatement.setInt(2, limit);
                priceStatement.execute();
                rows = priceStatement.getResultSet();
            } else {
                priceWhereStatement.setString(1, "%" + where + "%");
                priceWhereStatement.setInt(2, offset);
                priceWhereStatement.setInt(3, limit);
                priceWhereStatement.execute();
                rows = priceWhereStatement.getResultSet();
            }

            WebPrice price;

            while (rows.next()) {

                price = new WebPrice();

                price.setPercent(rows.getDouble("percent"));
                price.setLastPrice(rows.getDouble("lastprice"));
                price.setIdProd(rows.getInt("product_id"));
                price.setIdPrice(rows.getInt(1));
                price.setProduct(rows.getString("product"));
                price.setItem(rows.getString("item"));
                price.setRecommendedPrice(rows.getDouble("recommendedprice"));

                price.setOurPrice(new BigDecimal(price.getRecommendedPrice() * 0.77)
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());

                prices.add(price);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error get Prices: " + e.getMessage());
        }
        return prices;
    }*/

    public List<WebPrice> show(String where) {
        List<WebPrice> prices = new ArrayList<>();
        try {

            priceWhereStatement.setString(1, "%" + where + "%");
            priceWhereStatement.execute();
            ResultSet rows = priceWhereStatement.getResultSet();

            WebPrice price;

            while (rows.next()) {

                price = new WebPrice();

                price.setPercent(rows.getDouble("percent"));
                price.setLastPrice(rows.getDouble("lastprice"));
                price.setIdProd(rows.getInt("product_id"));
                price.setIdPrice(rows.getInt(1));
                price.setProduct(rows.getString("product"));
                price.setItem(rows.getString("item"));
                price.setRecommendedPrice(rows.getDouble("recommendedprice"));

                price.setOurPrice(new BigDecimal(price.getRecommendedPrice() * 0.77)
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());

                prices.add(price);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error get Prices: " + e.getMessage());
        }
        return prices;
    }
}
