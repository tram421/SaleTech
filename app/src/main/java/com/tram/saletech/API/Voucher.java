package com.tram.saletech.API;

public class Voucher {
    private int id;
    private String name;
    private String image;
    private int value;
    private String description;
    private String condition;

    public Voucher(int id, String name, String image, int value, String description, String condition) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.value = value;
        this.description = description;
        this.condition = condition;
    }

    public Voucher() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "Voucher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }
}
