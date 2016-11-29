package com.example.adamgyee.comicstripbuilder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.simplify.ink.InkView;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Stack;

public class DrawActivity extends AppCompatActivity {

    private int mCount;
    private int mNumArtists;
    private Button mFinished;
    private Button mBlue, mBlack, mWhite, mRed, mYellow, mGreen;
    private ArrayList<Bitmap> mBitmaps = new ArrayList<Bitmap>();

    @Override
    public void onBackPressed() {
        // Don't want users to mess up their masterpieces!
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        Intent intent = getIntent();
        mNumArtists = intent.getIntExtra("numArtists", 3);

        mCount = 0;
        // TODO: clear mBitmaps

        final InkView ink = (InkView) findViewById(R.id.ink);
        ink.setColor(getResources().getColor(android.R.color.black));
        ink.setMinStrokeWidth(1.5f);
        ink.setMaxStrokeWidth(6f);

        mFinished = (Button) findViewById(R.id.finished_drawing_btn);
        mFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameDone(mCount);
            }
        });

        mBlue = (Button) findViewById(R.id.blue_btn);
        mBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ink.setColor(getResources().getColor(android.R.color.holo_blue_light));
                ink.setMinStrokeWidth(1.5f);
                ink.setMaxStrokeWidth(6f);
            }
        });
        mBlack = (Button) findViewById(R.id.black_btn);
        mBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ink.setColor(getResources().getColor(android.R.color.black));
                ink.setMinStrokeWidth(1.5f);
                ink.setMaxStrokeWidth(6f);
            }
        });
        mWhite = (Button) findViewById(R.id.white_btn);
        mWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ink.setColor(getResources().getColor(android.R.color.white));
                ink.setMinStrokeWidth(10f);
                ink.setMaxStrokeWidth(16f);
            }
        });
        mYellow = (Button) findViewById(R.id.yellow_btn);
        mYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ink.setColor(getResources().getColor(android.R.color.holo_orange_light));
                ink.setMinStrokeWidth(1.5f);
                ink.setMaxStrokeWidth(6f);
            }
        });
        mGreen = (Button) findViewById(R.id.green_btn);
        mGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ink.setColor(getResources().getColor(android.R.color.holo_green_dark));
                ink.setMinStrokeWidth(1.5f);
                ink.setMaxStrokeWidth(6f);
            }
        });
        mRed = (Button) findViewById(R.id.red_btn);
        mRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ink.setColor(getResources().getColor(android.R.color.holo_red_dark));
                ink.setMinStrokeWidth(1.5f);
                ink.setMaxStrokeWidth(6f);
            }
        });

    }

    private void frameDone(int currentCount){

        final InkView ink = (InkView) findViewById(R.id.ink);

        // Grab image of canvas
        Bitmap current_drawing = ink.getBitmap();

        // Save image to bitmap
        mBitmaps.add(current_drawing);
        saveBitmap(current_drawing, currentCount);

        if (currentCount == mNumArtists-1){
            // This is the end state of the comic
            // Pass all bitmaps to FinalStrip activity to display
            // and call next intent
            Intent nextIntent = new Intent(this, FinalStrip.class);
            nextIntent.putExtra("numArtists", mNumArtists);
            for (int i = 0; i < mBitmaps.size(); i++){
                // bitmaps are too large to place in intentextras, so we need to compress them
                Bitmap image = mBitmaps.get(i);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 50, bs);
                nextIntent.putExtra("image" + i, bs.toByteArray());
            }
            startActivity(nextIntent);
        }

        // Set mini-display to the current drawing, then clear the canvas for the next drawing
        setPrevious(current_drawing);
        clearCanvas();
        mCount++;
    }

    private void saveBitmap(Bitmap current_drawing, int step){

        // TODO: Save to DB for later retrieval

        return;
        /*try {
            String image_string = Integer.toString(step);
            image_string += ".png";
            Log.d("")
            FileOutputStream stream = this.openFileOutput(image_string, Context.MODE_PRIVATE);
            mImage0.compress(Bitmap.CompressFormat.PNG, 100, stream);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void setPrevious(Bitmap current_drawing){
        // Show previous bitmap
        ImageView JOSH = (ImageView) findViewById(R.id.joshua);
        JOSH.setImageBitmap(current_drawing);
    }

    private void clearCanvas(){
        final InkView ink = (InkView) findViewById(R.id.ink);
        ink.clear();
    }
}
