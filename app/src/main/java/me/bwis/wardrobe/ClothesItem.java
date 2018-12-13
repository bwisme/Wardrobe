package me.bwis.wardrobe;

import java.util.ArrayList;

public class ClothesItem {

    public long id;
    public String name;
    public String photoPath;
    public String type;
    public ArrayList<String> seasons;
    public int color;
    public String colorType;
    public String storeLocation;
    public String brand;
    public double price;

    public ClothesItem(String name, String photoPath, String type, ArrayList<String> seasons,
                       int color, String colorType, String storeLocation, String brand, double price)
    {
        this.id = System.currentTimeMillis();
        this.name = name;
        this.photoPath = photoPath;
        this.type = type;
        this.seasons = seasons;
        this.color = color;
        this.colorType = colorType;
        this.storeLocation = storeLocation;
        this.brand = brand;
        this.price = price;
    }

    public ClothesItem(){

    }

}
