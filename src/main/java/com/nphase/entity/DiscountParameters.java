package com.nphase.entity;

/**
 * The representation of discount configuration both for product and category quantity discount.
 *
 * @author agorovtsov
 */
public class DiscountParameters {

    /**
     * To ease the configuration it is easier to set the minimal amount to reach discount conditions
     * instead of setting the negative border of the condition as it is done in the requirements
     */
    private int minimumDiscountAmount;
    private int discountPercents;

    public DiscountParameters(int minimumDiscountAmount, int discountPercents) {
        this.minimumDiscountAmount = minimumDiscountAmount;
        this.discountPercents = discountPercents;
    }

    public int getMinimumDiscountAmount() {
        return minimumDiscountAmount;
    }

    public int getDiscountPercents() {
        return discountPercents;
    }
}
