package by.spk.price.entity;

import lombok.Data;

@Data
public class WebPrice {

    private int idProd;
    private int idPrice;
    private String item;
    private String product;
    private double recommendedPrice;
    private double ourPrice;

//    private int lastPrice;
//    private int percent;
//    private int idSubCateg;
//    private int idCateg;
//    private String brand;
//    private String category;
//    private String subCategory;
}
