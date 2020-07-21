package com.example.axestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ViewFlipper v_flipper;

    RecyclerView recyclerView;

    String s1[],s2[];
    int gambar[] = {R.drawable.cupangflakat2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        s1 = getResources().getStringArray(R.array.Fish);
        s2 = getResources().getStringArray(R.array.Description);

        MyAdapter myAdapter = new MyAdapter(this, s1, s2, gambar);

        int images[] = {R.drawable.koi1, R.drawable.mas_koki1, R.drawable.molly_marble1};


        v_flipper = findViewById(R.id.v_flipper);

        for (int image: images){
            flipperImages(image);
        }

    }
    public void flipperImages(int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(3000);
        v_flipper.setAutoStart(true);

        //animation

        v_flipper.setInAnimation(this, android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }
}