package by.spk.price.entity;

import java.util.Objects;

public class Price {

    private int brandId;
    private int categoryId;
    private int subCategoryId;
    private int productId;
    private double recommendedPrice;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getRecommendedPrice() {
        return recommendedPrice;
    }

    public void setRecommendedPrice(double recommendedPrice) {
        this.recommendedPrice = recommendedPrice;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return brandId == price.brandId &&
                categoryId == price.categoryId &&
                subCategoryId == price.subCategoryId &&
                productId == price.productId &&
                Double.compare(price.recommendedPrice, recommendedPrice) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(brandId, categoryId, subCategoryId, productId, recommendedPrice);
    }
}
