package by.spk.price.entity;

import lombok.Data;

@Data
public class Price {

    private int brandId;
    private int categoryId;
    private int subCategoryId;
    private int productId;
    private int recommendedPrice;

}
