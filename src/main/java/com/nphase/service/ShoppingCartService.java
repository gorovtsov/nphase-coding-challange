package com.nphase.service;

import com.nphase.entity.DiscountParameters;
import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ShoppingCartService {

    public BigDecimal calculateTotalPrice(ShoppingCart shoppingCart) {
        return shoppingCart.getProducts()
                .stream()
                .map(product -> product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Task 2 solution.
     */
    public BigDecimal calculateBulkDiscountTotalPrice(ShoppingCart shoppingCart, DiscountParameters discountParameters) {
        return shoppingCart.getProducts()
                .stream()
                .map(product -> calculateBulkProductDiscount(product, discountParameters))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Task 3 solution
     *
     * @param shoppingCart
     * @return
     */
    public BigDecimal calculateCategoryDiscountTotalPrice(ShoppingCart shoppingCart, DiscountParameters discountParameters) {
        return shoppingCart.getProducts().stream()
                .collect(Collectors.groupingBy(Product::getCategory))
                .values().stream()
                .map(categoryProducts -> calculateCategoryProductsDiscount(categoryProducts, discountParameters))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateBulkProductDiscount(Product product, DiscountParameters discountParameters) {
        final BigDecimal noDiscountTotalPrice = calculateProductPrice(product);

        return product.getQuantity() >= discountParameters.getMinimumDiscountAmount()
                ? calculateDiscountPrice(noDiscountTotalPrice, discountParameters.getDiscountPercents())
                : noDiscountTotalPrice;
    }

    private BigDecimal calculateCategoryProductsDiscount(List<Product> products, DiscountParameters discountParameters) {
        final BigDecimal noDiscountTotalPrice =
                products.stream()
                        .map(this::calculateProductPrice)
                        .reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO);

        int totalCategoryQuantity = products.stream().mapToInt(Product::getQuantity).sum();

        return totalCategoryQuantity >= discountParameters.getMinimumDiscountAmount()
                ? calculateDiscountPrice(noDiscountTotalPrice, discountParameters.getDiscountPercents())
                : noDiscountTotalPrice;
    }

    private BigDecimal calculateProductPrice(Product product) {
        return product.getPricePerUnit().multiply(BigDecimal.valueOf(product.getQuantity()));
    }

    private BigDecimal calculateDiscountPrice(BigDecimal noDiscountPrice, Integer discountPercentage) {
        BigDecimal discountAmount = noDiscountPrice
                .multiply(BigDecimal.valueOf(discountPercentage))
                .divide(BigDecimal.valueOf(100));

        return noDiscountPrice.subtract(discountAmount);
    }
}
