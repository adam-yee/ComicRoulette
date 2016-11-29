package com.example.adamgyee.comicstripbuilder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileInputStream;

public class FinalStrip extends AppCompatActivity {

    private int mNumArtists;
    private LinearLayout mViewingPanel;

    @Override
    public void onBackPressed() {
        // TODO: send this back to the main menu
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_strip);

        mViewingPanel = (LinearLayout) findViewById(R.id.viewing_panel);

        Intent intent = getIntent();
        mNumArtists = intent.getIntExtra("numArtists", 3);

        // Retrieve the previous intent's bitmaps for fast displaying
        // we're also saving the bitmaps to a database in DrawActivity for future retrieval
        for (int i = 0; i < mNumArtists; i++){
            if (intent.hasExtra("image" + i)) {
                ImageView imageView = new ImageView(getApplicationContext());
                Bitmap bitmap = BitmapFactory.decodeByteArray(intent.getByteArrayExtra("image" + i), 0, intent.getByteArrayExtra("image" + i).length);
                imageView.setImageBitmap(bitmap);
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.layout_border));
                mViewingPanel.addView(imageView);
            }
        }

    }
}
