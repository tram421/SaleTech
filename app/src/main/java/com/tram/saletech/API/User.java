package com.tram.saletech.API;

public class User {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private Long idVoucher;
    private Long idOrder;
    private String idproduct;

    public User(Long id, String name, String address, String phone, Long idVoucher, Long idOrder, String idproduct) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.idVoucher = idVoucher;
        this.idOrder = idOrder;
        this.idproduct = idproduct;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }
    public String getIdproduct() {
        return idproduct;
    }
    public void setIdproduct(String idproduct) {
        this.idproduct = idproduct;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getIdVoucher() {
        return idVoucher;
    }

    public void setIdVoucher(Long idVoucher) {
        this.idVoucher = idVoucher;
    }

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", idVoucher=" + idVoucher +
                ", idOrder=" + idOrder +
                '}';
    }
}
