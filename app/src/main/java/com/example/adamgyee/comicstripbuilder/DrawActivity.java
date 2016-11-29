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

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Stack;

public class DrawActivity extends AppCompatActivity {

    private int mCount;
    private int mNumArtists;
    private Button mFinished;
    private Button mBlue, mBlack, mWhite, mRed, mYellow, mGreen;

    private ArrayList<Bitmap> mBitmaps = new ArrayList<Bitmap>();
    private Bitmap mImage0 = null;
    private Bitmap mImage1 = null;
    private Bitmap mImage2 = null;
    private Bitmap mImage3 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        Intent intent = getIntent();
        mNumArtists = intent.getIntExtra("numArtists", 3);

        mCount = 1;

        final InkView ink = (InkView) findViewById(R.id.ink);
        ink.setColor(getResources().getColor(android.R.color.black));
        ink.setMinStrokeWidth(1.5f);
        ink.setMaxStrokeWidth(6f);

        mFinished = (Button) findViewById(R.id.finished_drawing_btn);
        mFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentStep = mCount;
                nextStep(currentStep);
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

    private void nextStep(int prevStep){

        final InkView ink = (InkView) findViewById(R.id.ink);

        // Save current bitmap
        Bitmap prev_drawing = ink.getBitmap();

        if (prevStep < mNumArtists){
            // Working state of comic
            mBitmaps.add(prev_drawing);
            saveBitmap(prev_drawing, prevStep);
            setPrevious(prev_drawing);
            clearCanvas();
            mCount++;
        } else if (prevStep == mNumArtists){
            // This is the end state of the comic
            // Pass all bitmaps to FinalStrip activity to display
            // and call next intent
            Intent nextIntent = new Intent(this, FinalStrip.class);
            nextIntent.putExtra("numArtists", mNumArtists);
            for (int i = 0; i < mBitmaps.size(); i++){
                nextIntent.putExtra("image" + i, mBitmaps.get(i));
            }
            startActivity(nextIntent);
        }

        /*
        switch (currentStep){
            case 0:
                mImage0 = prev_drawing;
                setPrevious(prev_drawing);
                clearCanvas();
                mCount++;
                break;
            case 1:
                mImage1 = prev_drawing;
                setPrevious(prev_drawing);
                clearCanvas();
                mCount++;
                break;
            case 2:
                mImage2 = prev_drawing;
                setPrevious(prev_drawing);
                clearCanvas();
                mCount++;
                break;
            case 3:
                mImage3 = prev_drawing;
                clearCanvas();
                mCount = 0;

                // TODO: start new intent to show off final screen
                try {
                    //Write file
                    String image0_string = "image0.png";
                    FileOutputStream stream = this.openFileOutput(image0_string, Context.MODE_PRIVATE);
                    mImage0.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    String image1_string = "image1.png";
                    stream = this.openFileOutput(image1_string, Context.MODE_PRIVATE);
                    mImage1.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    String image2_string = "image2.png";
                    stream = this.openFileOutput(image2_string, Context.MODE_PRIVATE);
                    mImage2.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    String image3_string = "image3.png";
                    stream = this.openFileOutput(image3_string, Context.MODE_PRIVATE);
                    mImage3.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    //Cleanup
                    stream.close();
                    mImage0.recycle();
                    mImage1.recycle();
                    mImage2.recycle();
                    mImage3.recycle();

                    //Pop intent
                    Intent nextIntent = new Intent(this, FinalStrip.class);
                    nextIntent.putExtra("image0", image0_string);
                    nextIntent.putExtra("image1", image1_string);
                    nextIntent.putExtra("image2", image2_string);
                    nextIntent.putExtra("image3", image3_string);
                    startActivity(nextIntent);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            default:
                // Shouldn't ever be here
        }
        */



        // Clear image for next drawing
    }

    private void saveBitmap(Bitmap prev_drawing, int step){

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

    private void setPrevious(Bitmap prev_drawing){
        // Show previous bitmap
        ImageView JOSH = (ImageView) findViewById(R.id.joshua);
        JOSH.setImageBitmap(prev_drawing);
    }

    private void clearCanvas(){
        final InkView ink = (InkView) findViewById(R.id.ink);
        ink.clear();
    }
}
