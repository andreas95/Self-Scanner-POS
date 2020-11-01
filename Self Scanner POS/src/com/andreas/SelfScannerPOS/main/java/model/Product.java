package com.andreas.SelfScannerPOS.main.java.model;

import com.andreas.SelfScannerPOS.main.java.common.Shared;
import javafx.scene.control.Spinner;

public class Product {
    private int index;
    private String barcode;
    private String name;
    private int units;
    private int quantity;
    private String price;
    private String category;
    private String picture;
    private String total;

    public Product() {}
    public Product(String barcode, String name, int units, String price) {
        this.barcode = barcode;
        this.name = name;
        this.units = units;
        this.price = price;
    }
    public Product(String barcode, String name, int units, String price, String picture) {
        this.barcode = barcode;
        this.name = name;
        this.units = units;
        this.price = price;
        this.picture = picture;
    }
    public Product(int index, String barcode, String name, int units, String price) {
        this.index = index;
        this.barcode = barcode;
        this.name = name;
        this.units = units;
        this.price = price;
    }

    public String getBarcode() { return barcode; }
    public String getName() { return name; }
    public Spinner<Integer> getQuantity() {
        Spinner<Integer> spinner = new Spinner<>(0,units, quantity);
        spinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            quantity = newValue;
            setTotal();
        });
        spinner.setOnMouseClicked(e->Shared.updateCart.set(true));
        return spinner;

    }
    public String getPrice() { return price; }
    public String getCategory() { return category; }
    public String getPicture() { return picture; }
    public int getIndex() { return index; }
    public int getUnits() { return units; }
    public String getTotal() { return total; }
    public int getQuant() { return quantity; }

    public void setIndex(int i) { this.index = i;}
    public void setName(String name) { this.name = name; }
    public void setTotal() {
        this.total = Shared.currency + String.format ("%.2f",(100*quantity*Float.parseFloat(this.price.replace(Shared.currency,"")))/100);
    }
    public void setQuantity(int quantity) { this.quantity = quantity; setTotal(); }

}
