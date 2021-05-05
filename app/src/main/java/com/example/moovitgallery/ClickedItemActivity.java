package com.example.moovitgallery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ClickedItemActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicked_image);

        imageView = findViewById(R.id.imageView);

        Intent intent = getIntent();
        if(intent.getExtras()!=null){
            String selectedImage = intent.getStringExtra("image");

            Glide
                    .with(this)
                    .load(selectedImage)
                    .placeholder(R.drawable.placeholder).fitCenter()
                    .into(imageView);
        }


    }
}