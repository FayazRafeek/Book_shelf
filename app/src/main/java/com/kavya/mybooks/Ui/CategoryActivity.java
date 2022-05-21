package com.kavya.mybooks.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kavya.mybooks.Adapters.BookAdapter;
import com.kavya.mybooks.Models.Book;
import com.kavya.mybooks.SingletonRepo;
import com.kavya.mybooks.databinding.ActivityCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements BookAdapter.OnBookClick {


    ActivityCategoryBinding binding;

    FirebaseFirestore db;

    String catId,catName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i = getIntent();
        catId = i.getStringExtra("CAT_ID");
        catName = i.getStringExtra("CAT_NAME");

        binding.catTitle.setText(catName);

        fetchBooks();

        binding.catSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchBooks();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchBooks();
    }

    void fetchBooks(){

        binding.catSwipeRefresh.setRefreshing(true);

        CollectionReference cr = db.collection("Books");
        Query q = cr.whereEqualTo("catId",catId);
        q.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        binding.catSwipeRefresh.setRefreshing(false);
                        if(task.isSuccessful()){

                            List<Book> bukList = new ArrayList<>();
                            for(DocumentSnapshot doc : task.getResult()){
                                Book buk = doc.toObject(Book.class);
                                bukList.add(buk);
                            }
                            updateRecycler(bukList);

                            if(bukList.size() == 0){
                                binding.emptyUi.setVisibility(View.VISIBLE);
                            } else binding.emptyUi.setVisibility(View.GONE);
                        } else
                            Toast.makeText(CategoryActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    BookAdapter bukAdapter;
    void updateRecycler(List<Book> items){

        if(bukAdapter == null){
            bukAdapter = new BookAdapter(items, this,this);
            binding.booksRecycler.setAdapter(bukAdapter);
            binding.booksRecycler.setLayoutManager(new LinearLayoutManager(this));
        } else bukAdapter.updateList(items);
    }


    @Override
    public void onBookClick(Book book) {
        SingletonRepo.getInstance().setSelectedBook(book);
        startActivity(new Intent(this, BookActivity.class));
    }
}
