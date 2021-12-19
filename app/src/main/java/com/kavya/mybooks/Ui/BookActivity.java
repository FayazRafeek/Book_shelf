package com.kavya.mybooks.Ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.kavya.mybooks.Models.Book;
import com.kavya.mybooks.SingletonRepo;
import com.kavya.mybooks.databinding.ActivityBookBinding;

public class BookActivity extends AppCompatActivity {


    ActivityBookBinding binding;
    Book book;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        book = SingletonRepo.getInstance().getSelectedBook();
        if(book == null) finish();

        updateUi();

        binding.bukEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SingletonRepo.getInstance().setEditableBook(book);
                Intent intent = new Intent(BookActivity.this, NewBookActivity.class);
                intent.putExtra("IS_EDIT",true);
                startActivity(intent);
            }
        });

        binding.bukDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDelete();
            }
        });

        addToRecent();
    }

    void updateUi(){
        binding.bukCat.setText("Category : " + book.getCatName());
        binding.bukTitle.setText(book.getTitle());
        binding.bukContent.setText(book.getContent());

        if(book.getAuthor() != null)
            binding.bukAuth.setText("By : " + book.getAuthor());
        else
            binding.bukAuth.setText("By : Unknown");
    }

    FirebaseFirestore db;
    void startDelete() {

        db = FirebaseFirestore.getInstance();

        db.collection("Books")
                .document(book.getBookId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(BookActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else
                            Toast.makeText(BookActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    void addToRecent() {
        Gson gson = new Gson();
        String objString = gson.toJson(book);
        SharedPreferences pref = getSharedPreferences("MyPref",MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("RECENT",objString);
        editor.apply();
    }
}
