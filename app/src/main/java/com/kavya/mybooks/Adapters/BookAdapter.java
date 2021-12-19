package com.kavya.mybooks.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kavya.mybooks.Models.Book;
import com.kavya.mybooks.R;
import com.kavya.mybooks.databinding.BookListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookVH> {

    List<Book> items = new ArrayList<>();
    OnBookClick listner;
    Context context;

    public BookAdapter(List<Book> items, OnBookClick listner, Context context) {
        this.items = items;
        this.listner = listner;
        this.context = context;
    }

    @NonNull
    @Override
    public BookVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BookListItemBinding binding = BookListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new BookVH(binding,listner);
    }

    @Override
    public void onBindViewHolder(@NonNull BookVH holder, int position) {

        Book item = items.get(position);

        holder.binding.bukTitle.setText(item.getTitle());

        if(item.getAuthor() != null && item.getAuthor() != "")
            holder.binding.bukAuthor.setText("By : " + item.getAuthor());
        else
            holder.binding.bukAuthor.setText("By : Unknown");


        if(position % 5 == 0)
            holder.binding.bookCover.setBackgroundColor(context.getResources().getColor(R.color.book_cover_4));
        else if(position % 4 == 0)
            holder.binding.bookCover.setBackgroundColor(context.getResources().getColor(R.color.book_cover_1));
        else if(position % 3 == 0)
            holder.binding.bookCover.setBackgroundColor(context.getResources().getColor(R.color.book_cover_2));
        else if(position % 2 == 0)
            holder.binding.bookCover.setBackgroundColor(context.getResources().getColor(R.color.book_cover_3));
        else
            holder.binding.bookCover.setBackgroundColor(context.getResources().getColor(R.color.book_cover_1));

    }

    @Override
    public int getItemCount() {
        return items == null ? 0: items.size();
    }

    class BookVH extends RecyclerView.ViewHolder implements View.OnClickListener{

        BookListItemBinding binding;
        OnBookClick listner;

        public BookVH(@NonNull BookListItemBinding binding, OnBookClick listner) {
            super(binding.getRoot());

            this.binding = binding;
            this.listner = listner;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listner.onBookClick(items.get(getAdapterPosition()));
        }
    }

    public void updateList(List<Book> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public interface OnBookClick {
        void onBookClick(Book book);
    }
}
