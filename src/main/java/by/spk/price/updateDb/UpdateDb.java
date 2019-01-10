package by.spk.price.updateDb;

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

public final class UpdateDb {

    public void update() {

        final CsvDao csvDao = new CsvDao();
        csvDao.init();
        final List<Price> prices = new ArrayList<>();

        final Logger logger = LoggerFactory.getLogger(UpdateDb.class);

        logger.info("read file");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(new File(Utils.getPropertiesValue("csv.local.file"))),
                        "Windows-1251"))) {

            String line;
            reader.readLine(); // skip first line
            while ((line = reader.readLine()) != null) {

                line = line.replace("\"", "");

                final String[] strings = line.split(";");
                final Price price = new Price();

                // Бренд
                price.setBrandId(csvDao.getId(CsvDao.Tables.BRAND, strings[0]));
                // Категория (группа товаров)
                price.setCategoryId(csvDao.getId(CsvDao.Tables.CATEGORY, strings[1]));
                // Подкатегория
                price.setSubCategoryId(csvDao.getId(CsvDao.Tables.SUBCATEGORY, strings[2]));
                // Артикул товара
                // Название (артикул) товара
                price.setProductId(csvDao.getId(CsvDao.Tables.PRODUCT, strings[3], strings[4]));
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
        csvDao.setValue("updated", formatter.format(new Date()));
        csvDao.addPrices(prices);
        csvDao.finish();
    }
}
