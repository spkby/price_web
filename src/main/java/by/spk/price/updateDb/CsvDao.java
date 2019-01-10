package by.spk.price.updateDb;

import by.spk.price.JdbcConnection;
import by.spk.price.entity.Price;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvDao {

    enum Tables {
        BRAND, CATEGORY, SUBCATEGORY, PRODUCT
    }

    private Map<String, Long> brands;
    private Map<String, Long> categories;
    private Map<String, Long> subCategories;
    private Map<String, Long> products;

    private static final String UPDATE_VALUE = "UPDATE datas SET data_value = ? WHERE data_key = ?;";

    private static final String GET_BRANDS = "SELECT * FROM brands";
    private static final String GET_CATEGORIES = "SELECT * FROM categories";
    private static final String GET_SUBCATEGORIES = "SELECT * FROM subcategories";
    private static final String GET_PRODUCTS = "SELECT id,item FROM products";

    private static final String ADD_BRAND = "INSERT INTO brands (brand) VALUES(?)";
    private static final String ADD_CATEGORY = "INSERT INTO categories (category) VALUES(?)";
    private static final String ADD_SUBCATEGORY = "INSERT INTO subcategories (subcategory) VALUES(?)";
    private static final String ADD_PRODUCT = "INSERT INTO products (item,product) VALUES(?,?)";

    private static final String CLEAR_PRICES = "DELETE FROM prices";
    private static final String ADD_PRICE = "INSERT INTO prices (brand_id, category_id, subcategory_id,"
            + "product_id,recommended) VALUES(?,?,?,?,?)";

    private static Logger LOGGER = LoggerFactory.getLogger(CsvDao.class);

    private Connection getConnection() {
        return JdbcConnection.getInstance();
    }

    void init() {
        try {
            LOGGER.info("init");
            getConnection().setAutoCommit(false);
            fillMaps();
            clearPrices();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalStateException("error init");
        }
    }

    void finish() {
        try {
            getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalStateException("error finish");
        }
    }

    void setValue(final String key, final String value) {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_VALUE)) {
            statement.setNString(1, value);
            statement.setNString(2, key);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalArgumentException("Error set Value by Key(" + key + "): " + e.getMessage());
        }
    }

    Long getId(final Tables table, final String... values) {
        final Long id;
        final String sql;
        final Map<String, Long> map;
        switch (table) {
            case BRAND:
                sql = ADD_BRAND;
                map = brands;
                break;
            case CATEGORY:
                sql = ADD_CATEGORY;
                map = categories;
                break;
            case SUBCATEGORY:
                sql = ADD_SUBCATEGORY;
                map = subCategories;
                break;
            case PRODUCT:
                sql = ADD_PRODUCT;
                map = products;
                break;
            default:
                throw new IllegalArgumentException("Wrong 'case' in getId");
        }

        if (map.containsKey(values[0])) {
            id = map.get(values[0]);
        } else {
            id = add(table, map, sql, values);
        }
        return id;
    }

    private Long add(final Tables table, final Map<String, Long> map, final String sql, final String... values) {
        try (PreparedStatement statement = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setNString(1, values[0]);
            if (values.length > 1) {
                statement.setNString(2, values[1]);
            }
            statement.executeUpdate();
            try (ResultSet key = statement.getGeneratedKeys()) {
                Long id = null;
                if (key.next()) {
                    id = key.getLong(1);
                    map.put(values[0], id);
                }
                return id;
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalArgumentException("Error add new " + table.name() + ": " + values);
        }
    }

    void addPrices(final List<Price> prices) {
        try (PreparedStatement statement = getConnection().prepareStatement(ADD_PRICE)) {
            for (Price price : prices) {
                statement.setLong(1, price.getBrandId());
                statement.setLong(2, price.getCategoryId());
                statement.setLong(3, price.getSubCategoryId());
                statement.setLong(4, price.getProductId());
                statement.setLong(5, price.getRecommendedPrice());
                statement.addBatch();
            }
            LOGGER.info("execute batch");
            statement.executeBatch();

            getConnection().commit();
            LOGGER.info("commit");

        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalArgumentException("Error add new prices" + "\n" + e);
        }
    }

    private void clearPrices() {
        try (PreparedStatement statement = getConnection().prepareStatement(CLEAR_PRICES)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalArgumentException("Error clear prices");
        }
    }

    private Map<String, Long> fillMap(final String sql, final String field) {
        try (final PreparedStatement statement = getConnection().prepareStatement(sql)) {
            try (final ResultSet result = statement.executeQuery()) {
                final Map<String, Long> map = new HashMap<>();
                while (result.next()) {
                    map.put(result.getString(field), result.getLong("id"));
                }
                return map;
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            throw new IllegalArgumentException("Error create " + field + " maps");
        }
    }

    private void fillMaps() {
        brands = fillMap(GET_BRANDS, "brand");
        categories = fillMap(GET_CATEGORIES, "category");
        subCategories = fillMap(GET_SUBCATEGORIES, "subcategory");
        products = fillMap(GET_PRODUCTS, "item");

    }
}
