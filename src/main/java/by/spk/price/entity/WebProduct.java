package by.spk.price.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebProduct {

    private int id;
    private double lastPrice;
    private double percent;

}
