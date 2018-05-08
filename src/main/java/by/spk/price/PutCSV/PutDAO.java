package by.spk.price.PutCSV;

import by.spk.price.Entity.Price;
import by.spk.price.Utils;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PutDAO {

    private Map<String, Integer> brands;
    private Map<String, Integer> categories;
    private Map<String, Integer> subCategories;
    private Map<String, Integer> products;

    private static final String UPDATE_VALUE = "UPDATE data SET val = ? WHERE key = ?;";

    private static final String GET_BRANDS = "SELECT * FROM brands";
    private static final String GET_CATEGORIES = "SELECT * FROM categories";
    private static final String GET_SUBCATEGORIES = "SELECT * FROM subcategories";
    private static final String GET_PRODUCTS = "SELECT id,item FROM products";

    private static final String ADD_BRAND = "INSERT INTO brands (brand) VALUES(?)";
    private static final String ADD_CATEGORY = "INSERT INTO categories (category) VALUES(?)";
    private static final String ADD_SUBCATEGORY = "INSERT INTO subcategories (subcategory) VALUES(?)";
    private static final String ADD_PRODUCT = "INSERT INTO products (item,product) VALUES(?,?)";

    private static final String CLEAR_PRICES = "DELETE FROM prices";
    private static final String ADD_PRICE = "INSERT INTO prices (brand_id, category_id, subcategory_id," +
            "product_id,recommendedprice) VALUES(?,?,?,?,?)";

    private PreparedStatement addBrandStatement;
    private PreparedStatement addCategoryStatement;
    private PreparedStatement addSubCategoryStatement;
    private PreparedStatement addProductStatement;

    private PreparedStatement clearPricesStatement;
    private PreparedStatement addPriceStatement;

    private PreparedStatement updateValueStatement;

    private Statement statement;

    private Connection connection;

    public PutDAO() throws SQLException, IOException {

        connection = Utils.getConnection();
        connection.setAutoCommit(false);

        addBrandStatement = connection.prepareStatement(ADD_BRAND, Statement.RETURN_GENERATED_KEYS);
        addCategoryStatement = connection.prepareStatement(ADD_CATEGORY, Statement.RETURN_GENERATED_KEYS);
        addSubCategoryStatement = connection.prepareStatement(ADD_SUBCATEGORY, Statement.RETURN_GENERATED_KEYS);
        addProductStatement = connection.prepareStatement(ADD_PRODUCT, Statement.RETURN_GENERATED_KEYS);

        addPriceStatement = connection.prepareStatement(ADD_PRICE);
        clearPricesStatement = connection.prepareStatement(CLEAR_PRICES);

        updateValueStatement = connection.prepareStatement(UPDATE_VALUE);

        statement = connection.createStatement();

        fillMaps();
        clearPrices();
    }

    /*private void openConnection() throws IOException, SQLException {

        String url = Utils.getValue("db_url");
        String username = Utils.getValue("db_username");
        String password = Utils.getValue("db_password");

        connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(false);
    }*/

    public void setValue(String key, String value) {
        try {
            updateValueStatement.setString(1, value);
            updateValueStatement.setString(2, key);
            updateValueStatement.execute();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error set Value by Key(" + key + "): " + e.getMessage());
        }
    }

    public int getBrandId(String brand) {

        int id;

        if (brands.containsKey(brand)) {
            id = brands.get(brand);
        } else {
            id = addBrand(brand);
        }
        return id;
    }

    private int addBrand(String brand) {

        int id = -1;
        try {
            addBrandStatement.setString(1, brand);
            addBrandStatement.executeUpdate();

            ResultSet key = addBrandStatement.getGeneratedKeys();

            if (key.next()) {
                id = key.getInt(1);
                brands.put(brand, id);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error add new brand\n" + brand);
        }
        return id;
    }

    public int getProductId(String item, String product) {
        int id;

        if (products.containsKey(item)) {
            id = products.get(item);
        } else {
            id = addProduct(item, product);
        }
        return id;
    }

    private int addProduct(String item, String product) {

        int id = -1;
        try {
            addProductStatement.setString(1, item);
            addProductStatement.setString(2, product);
            addProductStatement.executeUpdate();

            ResultSet key = addProductStatement.getGeneratedKeys();

            if (key.next()) {
                id = key.getInt(1);
                products.put(item, id);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error add new product:\n" + item + "\n" + product);
        }
        return id;
    }

    public int getCategoryId(String category) {

        int id;

        if (categories.containsKey(category)) {
            id = categories.get(category);
        } else {
            id = addCategory(category);
        }
        return id;
    }

    private int addCategory(String category) {

        int id = -1;
        try {
            addCategoryStatement.setString(1, category);
            addCategoryStatement.executeUpdate();

            ResultSet key = addCategoryStatement.getGeneratedKeys();

            if (key.next()) {
                id = key.getInt(1);
                categories.put(category, id);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error add new category\n" + category);
        }
        return id;
    }

    public int getSubCategoryId(String subCategory) {

        int id;

        if (subCategories.containsKey(subCategory)) {
            id = subCategories.get(subCategory);
        } else {
            id = addSubCategory(subCategory);
        }
        return id;
    }

    private int addSubCategory(String subCategory) {

        int id = -1;
        try {
            addSubCategoryStatement.setString(1, subCategory);
            addSubCategoryStatement.executeUpdate();

            ResultSet key = addSubCategoryStatement.getGeneratedKeys();

            if (key.next()) {
                id = key.getInt(1);
                subCategories.put(subCategory, id);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error add new subcategory\n" + subCategory);
        }
        return id;
    }

    public void addPrices(List<Price> prices) {

        try {
            for (Price price : prices) {
                addPriceStatement.setInt(1, price.getBrandId());
                addPriceStatement.setInt(2, price.getCategoryId());
                addPriceStatement.setInt(3, price.getSubCategoryId());
                addPriceStatement.setInt(4, price.getProductId());
                addPriceStatement.setDouble(5, price.getRecommendedPrice());
                addPriceStatement.addBatch();
            }
            addPriceStatement.executeBatch();

            connection.commit();

        } catch (SQLException e) {
            throw new IllegalArgumentException("Error add new prices");
        }
    }

    private void clearPrices() {

        try {
            clearPricesStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error clear prices");
        }
    }

    private void fillMaps() throws SQLException {

        int id;
        String str;
        boolean success;
        ResultSet result;

        brands = new HashMap<>();
        success = statement.execute(GET_BRANDS);
        if (success) {
            result = statement.getResultSet();
            while (result.next()) {
                id = result.getInt("id");
                str = result.getString("brand");
                brands.put(str, id);
            }
        } else {
            throw new IllegalStateException("Error fill brands");
        }

        categories = new HashMap<>();
        success = statement.execute(GET_CATEGORIES);
        if (success) {
            result = statement.getResultSet();
            while (result.next()) {
                id = result.getInt("id");
                str = result.getString("category");
                categories.put(str, id);
            }
        } else {
            throw new IllegalStateException("Error fill categories");
        }

        subCategories = new HashMap<>();
        success = statement.execute(GET_SUBCATEGORIES);
        if (success) {
            result = statement.getResultSet();
            while (result.next()) {
                id = result.getInt("id");
                str = result.getString("subcategory");
                subCategories.put(str, id);
            }
        } else {
            throw new IllegalStateException("Error fill subcategories");
        }

        products = new HashMap<>();
        success = statement.execute(GET_PRODUCTS);
        if (success) {
            result = statement.getResultSet();
            while (result.next()) {
                id = result.getInt("id");
                str = result.getString("item");
                products.put(str, id);
            }
        } else {
            throw new IllegalStateException("Error fill products");
        }
    }
}