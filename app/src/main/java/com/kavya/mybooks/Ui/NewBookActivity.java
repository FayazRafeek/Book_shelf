package com.kavya.mybooks.Ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.kavya.mybooks.Models.Book;
import com.kavya.mybooks.Models.Category;
import com.kavya.mybooks.SingletonRepo;
import com.kavya.mybooks.databinding.ActivityNewBookBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewBookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ActivityNewBookBinding binding;


    String title, content, author; Category category;
    List<Category> categoryList;

    FirebaseFirestore db;

    Boolean isEdit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();

        binding = ActivityNewBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        isEdit = intent.getBooleanExtra("IS_EDIT",false);

        categoryList = SingletonRepo.INSTANCE.getCatList();

        setupSpinner();

        if(isEdit)
            initEditUi();

        binding.publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(validateInputs()){
                        Book buk = generateBook();
                        Log.d("BOOK ADD", buk.getBookId() + " " + buk.getCatId() + buk.getCatName());
                        startPublish(buk);
                    }
                } catch (Exception e) {
                    Log.e("BOOK ADD", e.getMessage(), e);
                }

            }
        });

        binding.draftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInputs()){
                    Book buk = generateBook();
                    saveTODraft(buk);
                }
            }
        });
    }

    void setupSpinner(){

        ArrayList<String> catString = new ArrayList<String>();
        for(Category c : categoryList)
            catString.add(c.getName());

        if(categoryList != null && categoryList.size() > 0)
            category = categoryList.get(0);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,catString);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.catSpinner.setAdapter(spinnerAdapter);

        binding.catSpinner.setOnItemSelectedListener(this);

    }

    Book generateBook() {
        String bookId = "";
        if(editId != null && editId != "")
            bookId = editId;
        else
            bookId = String.valueOf(System.currentTimeMillis());
        return new Book(bookId,title,content,category.getName(),category.getId(),author);
    }

    void startPublish(Book buk){

        showLoading();

        db.collection("Books")
                .document(buk.getBookId())
                .set(buk)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        hideLoading();
                        if(task.isSuccessful()){

                            Toast.makeText(NewBookActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            },600);
                        } else {
                            Toast.makeText(NewBookActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    void saveTODraft(Book buk){

        Gson gson = new Gson();
        String objString = gson.toJson(buk);
        SharedPreferences pref = getSharedPreferences("MyPref",MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("DRAFT",objString);
        editor.apply();

        Toast.makeText(this, "Added to draft", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },500);

    }

    Boolean validateInputs(){

        title = binding.newBukTitleInp.getText().toString();
        content = binding.newBukContentInp.getText().toString();
        author = binding.newBukAuthorInp.getText().toString();

        if(author != null && author.length() > 0)
            if(title != null && title.length() > 0)
                if(content != null && content.length() > 0)
                    return true;

        return false;
    }

    String editId = "";
    void initEditUi() {

        Book buk = SingletonRepo.getInstance().getEditableBook();
        if(buk == null) return;

        if(buk.getBookId() != null && !buk.getBookId().equals(""))
            editId = buk.getBookId();

        if(buk.getAuthor() != null)
            binding.newBukAuthorInp.setText(buk.getAuthor());
        if(buk.getTitle() != null)
            binding.newBukTitleInp.setText(buk.getTitle());
        if(buk.getContent() != null)
            binding.newBukContentInp.setText(buk.getContent());
        if(buk.getCatId() != null && !buk.getCatId().equals("")){

            int pos = 0;
            for(Category cat : categoryList){
                if(cat.getId().equals(buk.getCatId())){
                    category = cat;
                    break;
                }
                pos++;
            }

            binding.catSpinner.setSelection(pos);

        }


        binding.pageTitle.setText("Edit this book");

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        category = categoryList.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    void showLoading() { binding.newProgress.setVisibility(View.VISIBLE);}
    void hideLoading() { binding.newProgress.setVisibility(View.GONE);}
}
