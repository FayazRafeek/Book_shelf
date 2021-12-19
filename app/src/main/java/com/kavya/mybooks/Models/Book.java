package com.kavya.mybooks.Models;

public class Book {

    String bookId;
    String title;
    String content;
    String catName;
    String catId;
    String author;

    public Book(String bookId, String title, String content, String catName, String catId, String author) {
        this.bookId = bookId;
        this.title = title;
        this.content = content;
        this.catName = catName;
        this.catId = catId;
        this.author = author;
    }


    public Book() {
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
