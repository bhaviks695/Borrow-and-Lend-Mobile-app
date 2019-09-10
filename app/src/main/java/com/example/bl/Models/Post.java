package com.example.bl.Models;

public class Post {
    private String post_id;
    private String user_id;
    private String image;
    private String itemname;
    private String itemdesc;
    private String itemprice;
    private String itemlocation;
    private String phoneno;

    public Post(String post_id, String user_id, String image, String itemname, String itemdesc, String itemprice, String itemlocation, String phoneno) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.image = image;
        this.itemname = itemname;
        this.itemdesc = itemdesc;
        this.itemprice = itemprice;
        this.itemlocation = itemlocation;
        this.phoneno = phoneno;
    }

    public Post() {
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemdesc() {
        return itemdesc;
    }

    public void setItemdesc(String itemdesc) {
        this.itemdesc = itemdesc;
    }

    public String getItemprice() {
        return itemprice;
    }

    public void setItemprice(String itemprice) {
        this.itemprice = itemprice;
    }

    public String getItemlocation() {
        return itemlocation;
    }

    public void setItemlocation(String itemlocation) {
        this.itemlocation = itemlocation;
    }

    @Override
    public String toString() {
        return "Post{" +
                "post_id='" + post_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", image='" + image + '\'' +
                ", itemname='" + itemname + '\'' +
                ", itemdesc='" + itemdesc + '\'' +
                ", itemprice='" + itemprice + '\'' +
                ", itemlocation='" + itemlocation + '\'' +
                ", phoneno ='" + phoneno + '\'' +
                '}';
    }
}
