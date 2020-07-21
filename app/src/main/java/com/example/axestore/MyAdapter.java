package com.example.axestore;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyviewHolder> {

    String data1[], data2[];
    int gambar[];
    Context context;

    public MyAdapter (Context ct,String s1 [], String s2 [], int gmbr[]){
        context = ct;
        data1 = s1;
        data2 = s2;
        gambar = gmbr;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
