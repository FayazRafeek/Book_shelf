package com.kavya.mybooks.Models;

public class Category {

    String name;
    String id;
    String totalBooks;

    public Category(String name, String id, String totalBooks) {
        this.name = name;
        this.id = id;
        this.totalBooks = totalBooks;
    }

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(String totalBooks) {
        this.totalBooks = totalBooks;
    }
}
