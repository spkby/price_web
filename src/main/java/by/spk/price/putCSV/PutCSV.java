package by.spk.price.putCSV;

import by.spk.price.entity.Price;
import by.spk.price.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public final class PutCSV {

    private PutCSV() {
    }

    public static void put() {

        final PutDAO putDao = new PutDAO();
        putDao.init();

        final List<Price> prices = new ArrayList<>();

        String line;
        final Price price = new Price();
        final Logger logger = LoggerFactory.getLogger(PutCSV.class);

        logger.info("read file");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(new File(Utils.getPropertiesValue("csv.local.file"))),
                        "Windows-1251"))) {

            reader.readLine(); // skip first line
            while ((line = reader.readLine()) != null) {

                line = line.replace("\"", "");

                final String[] strings = line.split(";");
                //price = new Price();

                // Бренд
                price.setBrandId(putDao.getId(PutDAO.Tables.BRAND, strings[0]));
                // Категория (группа товаров)
                price.setCategoryId(putDao.getId(PutDAO.Tables.CATEGORY, strings[1]));
                // Подкатегория
                price.setSubCategoryId(putDao.getId(PutDAO.Tables.SUBCATEGORY, strings[2]));
                // Артикул товара
                // Название (артикул) товара
                price.setProductId(putDao.getId(PutDAO.Tables.PRODUCT, strings[3], strings[4]));
                // Цена
                price.setRecommendedPrice(Double.valueOf(Double.parseDouble(strings[5]) * 100).longValue());

                if (!prices.contains(price)) {
                    prices.add(price);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new IllegalStateException("Error parse file");
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Minsk"));
        putDao.setValue("updated", formatter.format(new Date()));
        putDao.addPrices(prices);
        putDao.finish();
    }
}
