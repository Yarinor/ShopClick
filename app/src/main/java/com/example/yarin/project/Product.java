package com.example.yarin.project;

/**
 * Created by Yarin on 09/05/2017.
 */

public class Product {
    private String productName;
    private String price;
    private String amount ="1";
    private String owner;
    private String intialPrice;

    public String getIntialPrice() {
        return intialPrice;
    }

    public void setIntialPrice(String intialPrice) {
        this.intialPrice = intialPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {this.productName = productName;}

    public String getPrice() {return this.price;}

    public void setPrice(String price) {this.price=price;}

    public String getAmount() {return this.amount;}

    public void setAmount(String amount) {this.amount=amount;}

    public String getOwner() {return owner;}

    public void setOwner(String owner) {this.owner = owner;}
}
