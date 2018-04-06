package by.spk.price.PutCSV;

import by.spk.price.Entity.Price;
import by.spk.price.Utils;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class PutCSV {

    public static void put() throws IOException, SQLException {

        PutDAO putDao = new PutDAO();
        List<Price> prices = new ArrayList<>();

        String line;
        String[] strings;
        Price price;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(new File(Utils.getPropertiesValue("csv_filepath"))),
                        "Windows-1251"))) {

            reader.readLine(); // skip first line
            while ((line = reader.readLine()) != null) {
                line = line.replace("\"", "");

                strings = line.split(";");

                price = new Price();

                // Бренд
                price.setBrandId(putDao.getBrandId(strings[0]));
                // Категория (группа товаров)
                price.setCategoryId(putDao.getCategoryId(strings[1]));
                // Подкатегория
                price.setSubCategoryId(putDao.getSubCategoryId(strings[2]));
                // Артикул товара
                // Название (артикул) товара
                price.setProductId(putDao.getProductId(strings[3], strings[4]));
                // Цена
                price.setRecommendedPrice(Double.parseDouble(strings[5]));

                if (!prices.contains(price)) {
                    prices.add(price);
                }
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Minsk"));
        putDao.setValue("updated", formatter.format(new Date()));
        putDao.addPrices(prices);
    }
}
