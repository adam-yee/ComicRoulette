package com.example.adamgyee.comicstripbuilder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.FileInputStream;

public class FinalStrip extends AppCompatActivity {

    private Bitmap mImage0, mImage1, mImage2, mImage3;
    private ImageView mView0, mView1, mView2, mView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_strip);

        mView0 = (ImageView) findViewById(R.id.image0);
        mView1 = (ImageView) findViewById(R.id.image1);
        mView2 = (ImageView) findViewById(R.id.image2);
        mView3 = (ImageView) findViewById(R.id.image3);

        String image0 = getIntent().getStringExtra("image0");
        try {
            FileInputStream is = this.openFileInput(image0);
            mImage0 = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String image1 = getIntent().getStringExtra("image1");
        try {
            FileInputStream is = this.openFileInput(image1);
            mImage1 = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String image2 = getIntent().getStringExtra("image2");
        try {
            FileInputStream is = this.openFileInput(image2);
            mImage2 = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String image3 = getIntent().getStringExtra("image3");
        try {
            FileInputStream is = this.openFileInput(image3);
            mImage3 = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mView0.setImageBitmap(mImage0);
        mView1.setImageBitmap(mImage1);
        mView2.setImageBitmap(mImage2);
        mView3.setImageBitmap(mImage3);

    }
}
