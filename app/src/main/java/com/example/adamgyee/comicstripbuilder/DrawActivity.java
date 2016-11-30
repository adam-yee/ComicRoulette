package com.example.adamgyee.comicstripbuilder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    protected void onStop() {
        super.onStop();

        for (int i = 0; i < mBitmaps.size(); i++) {
            if (mBitmaps.get(i) != null && !mBitmaps.get(i).isRecycled()) {
                Log.d("recyclin","yeahp");
                mBitmaps.get(i).recycle();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        Intent intent = getIntent();
        mNumArtists = intent.getIntExtra("numArtists", 3);

        mCount = 0;
        getSupportActionBar().setTitle(getResources().getString(R.string.frame_num, mCount+1, mNumArtists));

        /*
        if (mBitmaps.size() > 0){
            for (int i = 0; i < mBitmaps.size(); i++){
                mBitmaps.get(i).recycle();
                mBitmaps.set(i, null);
            }
            mBitmaps.clear();
        }
        */
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
            startActivity(nextIntent);
        } else {
            // Set action bar to current frame number
            getSupportActionBar().setTitle(getResources().getString(R.string.frame_num, mCount+2, mNumArtists));
            // Set ink color back to black
            mBlack.callOnClick();
            // Set mini-display to the current drawing, then clear the canvas for the next drawing
            setPrevious(current_drawing);
            clearCanvas();
            mCount++;
        }
    }

    public String saveBitmap(Bitmap bitmap, int i) {

        String fileName = "image" + i + ".png";
        Log.d("saving image:", fileName);
        try {
            FileOutputStream stream = this.openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    private void setPrevious(Bitmap current_drawing){
        // Show previous bitmap
        ImageView imageView = (ImageView) findViewById(R.id.joshua);
        imageView.setImageBitmap(current_drawing);
    }

    private void clearCanvas(){
        final InkView ink = (InkView) findViewById(R.id.ink);
        ink.clear();
    }
}
