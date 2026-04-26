package ordersystem.util;

import ordersystem.model.Product;
import java.math.BigDecimal;
import java.util.List;

public class PriceCalculator {

    private PriceCalculator() {}

    public static BigDecimal calculateTotal(List<Product> products) {
        return products.stream()
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}