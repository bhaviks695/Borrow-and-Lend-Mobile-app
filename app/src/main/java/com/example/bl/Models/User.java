package com.example.bl.Models;

public class User {

  public  String name;
  public String phoneno;
  public String rating;

    public User(String name, String phoneno, String rating) {
        this.name = name;
        this.phoneno = phoneno;
        this.rating = rating;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

}
