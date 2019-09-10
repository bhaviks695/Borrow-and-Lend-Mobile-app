package com.example.bl.Models;

public class Brwlend {
    private String requestid;
    private String borrowername;
    private String itemname;
    private String itemid;
    private String borrowerid;
    private String lenderid;
    private String hours;
    private String accept;
    private String price;

    public Brwlend(String borrowername,String itemname, String itemid, String borrowerid, String lenderid, String hours, String accept, String requestid, String price) {
        this.borrowername = borrowername;
        this.itemname = itemname;
        this.itemid = itemid;
        this.borrowerid = borrowerid;
        this.lenderid = lenderid;
        this.hours = hours;
        this.accept = accept;
        this.requestid = requestid;
        this.price = price;
    }

    public Brwlend() {
    }

    public String getBorrowername() {
        return borrowername;
    }

    public void setBorrowername(String borrowername) {
        this.borrowername = borrowername;
    }


    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getBorrowerid() {
        return borrowerid;
    }

    public void setBorrowerid(String borrowerid) {
        this.borrowerid = borrowerid;
    }

    public String getLenderid() {
        return lenderid;
    }

    public void setLenderid(String lenderid) {
        this.lenderid = lenderid;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
