package by.spk.price.web;

import by.spk.price.JdbcConnection;
import by.spk.price.entity.WebPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WebDAO {

    private static final String PRICE_WHERE = "select prices.id as id, subcategory, item, product,"
            + " recommended, product_id from prices"
            + " inner join products on prices.product_id = products.id"
            + " inner join subcategories on prices.subcategory_id = subcategories.id"
            + " where LOWER(product) like LOWER(?);";

    private static final String SELECT_VALUE = "select data_value from datas where data_key = ?;";

    private static Logger LOGGER = LoggerFactory.getLogger(WebDAO.class);

    protected Connection getConnection() {
        return JdbcConnection.getInstance();
    }

    public String getValue(final String key) {
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT_VALUE)) {
            statement.setString(1, key);
            try (ResultSet rows = statement.executeQuery()) {
                String value = "";
                if (rows.next()) {
                    value = rows.getString(1);
                }
                return value;
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalArgumentException("Error get Value by Key(" + key + "): " + e.getMessage());
        }
    }

    public List<WebPrice> show(final String where) {
        try (PreparedStatement statement = getConnection().prepareStatement(PRICE_WHERE)) {
            statement.setString(1, "%" + where + "%");
            try (ResultSet rows = statement.executeQuery()) {
                final List<WebPrice> prices = new ArrayList<>();
                while (rows.next()) {
                    final WebPrice price = new WebPrice();
                    price.setIdProd(rows.getInt("product_id"));
                    price.setIdPrice(rows.getInt(1));
                    price.setProduct(rows.getString("product"));
                    price.setItem(rows.getString("item"));
                    price.setRecommendedPrice(rows.getDouble("recommended") / 100);
                    price.setOurPrice(new BigDecimal(price.getRecommendedPrice() * 0.77)
                            .setScale(2, RoundingMode.HALF_UP).doubleValue());
                    prices.add(price);
                }
                return prices;
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalArgumentException("Error get Prices: " + e.getMessage());
        }
    }
}
