package by.spk.price.entity;

public class WebProduct {

    private int id;
    private double lastPrice;
    private double percent;

    public WebProduct(int id, double lastPrice, double percent) {
        this.id = id;
        this.lastPrice = lastPrice;
        this.percent = percent;
    }

    public int getId() {
        return id;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public double getPercent() {
        return percent;
    }
}
