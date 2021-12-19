package com.kavya.mybooks.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kavya.mybooks.Models.Category;
import com.kavya.mybooks.R;
import com.kavya.mybooks.databinding.CatItemBinding;

import java.util.List;

public class CatergoryAdapter extends RecyclerView.Adapter<CatergoryAdapter.CategoryVH> {

    List<Category> items;
    OnCategoryClick listner;
    Context context;

    public CatergoryAdapter(List<Category> items, OnCategoryClick listner,Context context) {
        this.items = items;
        this.listner = listner;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CatItemBinding binding = CatItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new CategoryVH(binding,listner);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVH holder, int position) {
        Category item = items.get(position);
        String title = item.getName();
        holder.binding.catTitle.setText(item.getName());

        if(title.equals("Drama"))
            Glide.with(context)
                    .load(R.drawable.drama)
                    .into(holder.binding.catImage);
        else if(title.equals("Classics"))
            Glide.with(context)
                    .load(R.drawable.classic)
                    .into(holder.binding.catImage);
        else if(title.equals("Fantasy"))
            Glide.with(context)
                    .load(R.drawable.fantasy)
                    .into(holder.binding.catImage);
        else if(title.equals("Fiction"))
            Glide.with(context)
                    .load(R.drawable.fiction)
                    .into(holder.binding.catImage);
        else if(title.equals("Adventure"))
            Glide.with(context)
                    .load(R.drawable.adventure)
                    .into(holder.binding.catImage);


    }

    @Override
    public int getItemCount() {
        return items != null? items.size() : 0;
    }

    public void updateList(List<Category> newItems){
        items = newItems;
        notifyDataSetChanged();
    }

    class CategoryVH extends RecyclerView.ViewHolder implements View.OnClickListener{

        CatItemBinding binding;
        OnCategoryClick listner;
        public CategoryVH(@NonNull CatItemBinding binding, OnCategoryClick listner) {
            super(binding.getRoot());
            this.binding = binding;
            this.listner = listner;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listner.onCategoryClick(items.get(getAdapterPosition()));
        }
    }

    public interface OnCategoryClick {
        void onCategoryClick(Category category);
    }
}
