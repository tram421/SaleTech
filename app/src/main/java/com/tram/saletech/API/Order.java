package com.tram.saletech.API;

public class Order {
    /*
    $row['id'],
                    $row['idUser'],
                    $row['listCart'],
                    $row['idVoucher'],
                    $row['bill'],
                    $row['status']
     */
    private int id;
    private int idUser;
    private String lisCart;
    private int idVoucher;
    private int bill;
    private int status;
    private String description;

    public Order(int id, int idUser, String lisCart, int idVoucher, int bill, int status, String description) {
        this.id = id;
        this.idUser = idUser;
        this.lisCart = lisCart;
        this.idVoucher = idVoucher;
        this.bill = bill;
        this.status = status;
        this.description = description;
    }
    public Order(int id, int status, String description) {
        this.id = id;
        this.idUser = idUser;
        this.lisCart = lisCart;
        this.idVoucher = idVoucher;
        this.bill = bill;
        this.status = status;
        this.description = description;
    }

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getLisCart() {
        return lisCart;
    }

    public void setLisCart(String lisCart) {
        this.lisCart = lisCart;
    }

    public int getIdVoucher() {
        return idVoucher;
    }

    public void setIdVoucher(int idVoucher) {
        this.idVoucher = idVoucher;
    }

    public int getBill() {
        return bill;
    }

    public void setBill(int bill) {
        this.bill = bill;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", lisCart='" + lisCart + '\'' +
                ", idVoucher=" + idVoucher +
                ", bill=" + bill +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
