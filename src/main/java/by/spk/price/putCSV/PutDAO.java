package by.spk.price.putCSV;

import by.spk.price.JdbcConnection;
import by.spk.price.entity.Price;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PutDAO {

    private Map<String, Integer> brands;
    private Map<String, Integer> categories;
    private Map<String, Integer> subCategories;
    private Map<String, Integer> products;

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

    protected Connection getConnection() {
        return JdbcConnection.getInstance();
    }

    protected void close(final Statement statement, final ResultSet resultSet) {
        JdbcConnection.close(statement, resultSet);
    }

    public PutDAO() {

    }

    public void init() {
        try {
            getConnection().setAutoCommit(false);
            fillMaps();
            clearPrices();
        } catch (SQLException e) {
            throw new IllegalStateException("error init");
        }
    }

    public void finish() {
        try {
            getConnection().setAutoCommit(true);
        } catch (SQLException e) {
            throw new IllegalStateException("error finish");
        }
    }

    public void setValue(final String key, final String value) {
        PreparedStatement statement = null;

        try {
            statement = getConnection().prepareStatement(UPDATE_VALUE);
            statement.setNString(1, value);
            statement.setNString(2, key);
            statement.execute();

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error set Value by Key(" + key + "): " + e.getMessage());
        } finally {
            close(statement, null);
        }
    }

    public int getBrandId(final String brand) {

        int id;

        if (brands.containsKey(brand)) {
            id = brands.get(brand);
        } else {
            id = addBrand(brand);
        }
        return id;
    }

    private int addBrand(final String brand) {
        PreparedStatement statement = null;
        ResultSet key = null;
        int id = -1;
        try {
            statement = getConnection().prepareStatement(ADD_BRAND, Statement.RETURN_GENERATED_KEYS);

            statement.setNString(1, brand);
            statement.executeUpdate();

            key = statement.getGeneratedKeys();

            if (key.next()) {
                id = key.getInt(1);
                brands.put(brand, id);
            }

            return id;

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error add new brand: " + brand);
        } finally {
            close(statement, key);
        }
    }

    public int getProductId(final String item, final String product) {
        int id;

        if (products.containsKey(item)) {
            id = products.get(item);
        } else {
            id = addProduct(item, product);
        }
        return id;
    }

    private int addProduct(final String item, final String product) {
        PreparedStatement statement = null;
        ResultSet key = null;

        int id = -1;
        try {
            statement = getConnection().prepareStatement(ADD_PRODUCT, Statement.RETURN_GENERATED_KEYS);

            statement.setNString(1, item);
            statement.setNString(2, product);
            statement.executeUpdate();

            key = statement.getGeneratedKeys();

            if (key.next()) {
                id = key.getInt(1);
                products.put(item, id);
            }
            return id;

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error add new product:\n" + item + "\n" + product);
        } finally {
            close(statement, key);
        }
    }

    public int getCategoryId(final String category) {

        int id;

        if (categories.containsKey(category)) {
            id = categories.get(category);
        } else {
            id = addCategory(category);
        }
        return id;
    }

    private int addCategory(final String category) {
        PreparedStatement statement = null;
        ResultSet key = null;
        int id = -1;
        try {
            statement = getConnection().prepareStatement(ADD_CATEGORY, Statement.RETURN_GENERATED_KEYS);

            statement.setNString(1, category);
            statement.executeUpdate();

            key = statement.getGeneratedKeys();

            if (key.next()) {
                id = key.getInt(1);
                categories.put(category, id);
            }

            return id;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error add new category: " + category);
        } finally {
            close(statement, key);
        }
    }

    public int getSubCategoryId(final String subCategory) {

        int id;

        if (subCategories.containsKey(subCategory)) {
            id = subCategories.get(subCategory);
        } else {
            id = addSubCategory(subCategory);
        }
        return id;
    }

    private int addSubCategory(final String subCategory) {
        PreparedStatement statement = null;
        ResultSet key = null;

        int id = -1;
        try {
            statement = getConnection().prepareStatement(ADD_SUBCATEGORY, Statement.RETURN_GENERATED_KEYS);

//            statement.setString(1, "Тест");
            statement.setString(1, subCategory);
            statement.executeUpdate();

            key = statement.getGeneratedKeys();

            if (key.next()) {
                id = key.getInt(1);
                subCategories.put(subCategory, id);
            }
            return id;

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error add new subcategory: " + subCategory);
        } finally {
            close(statement, key);
        }
    }

    public void addPrices(final List<Price> prices) {
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(ADD_PRICE);

            for (Price price : prices) {
                statement.setInt(1, price.getBrandId());
                statement.setInt(2, price.getCategoryId());
                statement.setInt(3, price.getSubCategoryId());
                statement.setInt(4, price.getProductId());
                statement.setInt(5, price.getRecommendedPrice());
                statement.addBatch();
            }
            statement.executeBatch();

            getConnection().commit();

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error add new prices" + "\n" + e);
        } finally {
            close(statement, null);
        }
    }

    private void clearPrices() {
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(CLEAR_PRICES);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error clear prices");
        } finally {
            close(statement, null);
        }
    }

    private void fillBrands(Statement statement, ResultSet result) throws SQLException {

        brands = new HashMap<>();
        statement.execute(GET_BRANDS);
        result = statement.getResultSet();
        while (result.next()) {
            brands.put(result.getString("brand"), result.getInt("id"));
        }
    }

    private void fillCategories(Statement statement, ResultSet result) throws SQLException {

        categories = new HashMap<>();
        statement.execute(GET_CATEGORIES);
        result = statement.getResultSet();
        while (result.next()) {
            categories.put(result.getString("category"), result.getInt("id"));
        }
    }

    private void fillSubCategories(final Statement statement, ResultSet result) throws SQLException {

        subCategories = new HashMap<>();
        statement.execute(GET_SUBCATEGORIES);
        result = statement.getResultSet();
        while (result.next()) {
            subCategories.put(result.getString("subcategory"), result.getInt("id"));
        }
    }

    private void fillProducts(final Statement statement, ResultSet result) throws SQLException {

        products = new HashMap<>();
        statement.execute(GET_PRODUCTS);
        result = statement.getResultSet();
        while (result.next()) {
            products.put(result.getString("item"), result.getInt("id"));
        }
    }

    private void fillMaps() {
        Statement statement = null;
        ResultSet result = null;

        try {
            statement = getConnection().createStatement();

            fillBrands(statement, result);
            fillCategories(statement, result);
            fillSubCategories(statement, result);
            fillProducts(statement, result);

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error create maps");
        } finally {
            close(statement, result);
        }
    }
}
