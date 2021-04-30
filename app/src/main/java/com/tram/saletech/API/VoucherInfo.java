package com.tram.saletech.API;

public class VoucherInfo {
    private static VoucherInfo instance;
    public String imgVoucher;
    public  int valueVoucher;
    public  String nameVoucher;
    public int idVoucher;
    private VoucherInfo(){

    }
    public static VoucherInfo getInstance()
    {
        if (instance == null) {
            instance = new VoucherInfo();
        }
        return instance;
    }

}
