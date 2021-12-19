package com.kavya.mybooks;

import com.kavya.mybooks.Models.Book;
import com.kavya.mybooks.Models.Category;

import java.util.ArrayList;
import java.util.List;

public class SingletonRepo {

    public static SingletonRepo INSTANCE = new SingletonRepo();

    public static SingletonRepo getInstance() {
        if(INSTANCE == null){
            return new SingletonRepo();
        } else return INSTANCE;
    }


//   Global datas
    List<Category> catList = new ArrayList<>();

    public void setCatList(List<Category> catList) {
        this.catList = catList;
    }

    public List<Category> getCatList() {
        return catList;
    }

    Book selectedBook;

    public Book getSelectedBook() {
        return selectedBook;
    }

    public void setSelectedBook(Book selectedBook) {
        this.selectedBook = selectedBook;
    }

    Book editableBook;

    public Book getEditableBook() {
        return editableBook;
    }

    public void setEditableBook(Book editableBook) {
        this.editableBook = editableBook;
    }


    Book recentBook;

    public Book getRecentBook() {
        return recentBook;
    }

    public void setRecentBook(Book recentBook) {
        this.recentBook = recentBook;
    }
}
