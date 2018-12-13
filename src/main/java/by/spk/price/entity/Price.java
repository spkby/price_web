package by.spk.price.entity;

import lombok.Data;

@Data
public class Price {

    private Long brandId;
    private Long categoryId;
    private Long subCategoryId;
    private Long productId;
    private Long recommendedPrice;

}
