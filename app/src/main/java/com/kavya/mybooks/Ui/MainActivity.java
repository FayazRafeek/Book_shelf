package com.kavya.mybooks.Ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.kavya.mybooks.Adapters.CatergoryAdapter;
import com.kavya.mybooks.Models.Book;
import com.kavya.mybooks.Models.Category;
import com.kavya.mybooks.SingletonRepo;
import com.kavya.mybooks.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CatergoryAdapter.OnCategoryClick {

    ActivityMainBinding binding;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    SharedPreferences pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        pref = getSharedPreferences("MyPref",MODE_PRIVATE);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buttonClicks();

        fetchCategories();
        draftUi();
        recentUi();
    }


    @Override
    protected void onResume() {
        super.onResume();
        draftUi();
        recentUi();
    }

    void buttonClicks(){

        binding.writeBukBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NewBookActivity.class));
            }
        });


        binding.mainSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchCategories();
            }
        });

        binding.userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserActivity.class));
            }
        });

    }





    void fetchCategories(){

        binding.mainSwipeRefresh.setRefreshing(true);

        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        binding.mainSwipeRefresh.setRefreshing(false);
                        if(task.isSuccessful()){

                            List<Category> newList = new ArrayList<>();
                            for(DocumentSnapshot doc : task.getResult()){
                                Category item = doc.toObject(Category.class);
                                newList.add(item);
                            }
                            updateCatRecycler(newList);

                        } else
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    CatergoryAdapter catAdapter;
    void updateCatRecycler(List<Category> items){

        SingletonRepo.getInstance().setCatList(items);
        if(catAdapter == null){

            catAdapter = new CatergoryAdapter(items,this,this);
            binding.catRecycler.setAdapter(catAdapter);
            binding.catRecycler.setLayoutManager(new GridLayoutManager(this,2));

        } else catAdapter.updateList(items);

    }

    Book draftBuk;
    void draftUi() {

        String draft = pref.getString("DRAFT","");
        if(!draft.equals("")){
            Gson gson = new Gson();
            draftBuk = gson.fromJson(draft, Book.class);

            Log.d("BOOK_DRAFT", "draftUi: " + draftBuk.getTitle());

            binding.draftBukTitle.setText(draftBuk.getTitle());
            binding.draftBukContent.setText(draftBuk.getContent());

            binding.draftBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SingletonRepo.getInstance().setEditableBook(draftBuk);
                    Intent intent = new Intent(MainActivity.this, NewBookActivity.class);
                    intent.putExtra("IS_EDIT",true);
                    startActivity(intent);
                }
            });
        } else {
            binding.draftTitle.setText("No books found");
        }



    }


    Book recentBuk;
    void recentUi() {

        String recent = pref.getString("RECENT","");
        if(!recent.equals("")){
            Gson gson = new Gson();
            recentBuk = gson.fromJson(recent, Book.class);

            binding.recentBukTitle.setText(recentBuk.getTitle());
            binding.recentBukContent.setText(recentBuk.getContent());

            binding.recentBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SingletonRepo.getInstance().setSelectedBook(recentBuk);
                    startActivity(new Intent(MainActivity.this, BookActivity.class));
                }
            });
        } else {
            binding.recentBukTitle.setText("No books found");
        }



    }

    @Override
    public void onCategoryClick(Category category) {
        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
        intent.putExtra("CAT_ID", category.getId());
        intent.putExtra("CAT_NAME", category.getName());
        startActivity(intent);
    }
}
