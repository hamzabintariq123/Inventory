package com.hamza.inventory.Date_Models;

/**
 * Created by Hamza Android on 11/10/2016.
 */
public class Parcel_model {


    String saleman_id,product_date,Product,Quantity,retail_price,trade_price;

    public String getSaleman_id() {
        return saleman_id;
    }

    public void setSaleman_id(String saleman_id) {
        this.saleman_id = saleman_id;
    }

    public String getProduct_date() {
        return product_date;
    }

    public void setProduct_date(String product_date) {
        this.product_date = product_date;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getRetail_price() {
        return retail_price;
    }

    public void setRetail_price(String retail_price) {
        this.retail_price = retail_price;
    }

    public String getTrade_price() {
        return trade_price;
    }

    public void setTrade_price(String trade_price) {
        this.trade_price = trade_price;
    }
}
