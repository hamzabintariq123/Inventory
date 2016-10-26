package com.hamza.inventory.Date_Models;

/**
 * Created by Harry on 10/26/2016.
 */
public class Sale_model {


    String ProductName,ProductRate,ProductAmount,ProductQuantity;

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        ProductQuantity = productQuantity;
    }

    public String getProductRate() {
        return ProductRate;
    }

    public void setProductRate(String productRate) {
        ProductRate = productRate;
    }

    public String getProductAmount() {
        return ProductAmount;
    }

    public void setProductAmount(String productAmount) {
        ProductAmount = productAmount;
    }
}
