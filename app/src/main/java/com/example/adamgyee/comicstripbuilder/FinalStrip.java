package com.example.adamgyee.comicstripbuilder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;

public class FinalStrip extends AppCompatActivity {

    private int mNumArtists;
    private LinearLayout mViewingPanel;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save) {
            Toast.makeText(getApplicationContext(), "Saving comic to gallery", Toast.LENGTH_SHORT).show();
            saveComicToGallery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean saveComicToGallery(){
        // TODO: implement save
        return false;
    }

    @Override
    public void onBackPressed() {
        // TODO: send this back to the main menu
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();

        LinearLayout layout = (LinearLayout)findViewById(R.id.viewing_panel);

        for (int i = 0; i < layout.getChildCount(); i++) {
            ImageView v = (ImageView) layout.getChildAt(i);
            if (((BitmapDrawable)v.getDrawable()).getBitmap() != null && !((BitmapDrawable)v.getDrawable()).getBitmap().isRecycled()) {
                Log.d("recyclin", "yup");
                ((BitmapDrawable) v.getDrawable()).getBitmap().recycle();
            }
        }

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

            try {
                ImageView imageView = new ImageView(getApplicationContext());
                FileInputStream is = this.openFileInput("image" + i + ".png");
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                is.close();
                imageView.setImageBitmap(bitmap);
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.layout_border));
                mViewingPanel.addView(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*
            try {
                ImageView imageView = new ImageView(getApplicationContext());
                Bitmap bitmap = BitmapFactory.decodeStream(getApplicationContext().openFileInput("image"+i));
                imageView.setImageBitmap(bitmap);
                //imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.layout_border));
                mViewingPanel.addView(imageView);
            } catch (IOException e) {
                Log.e("exception:", e.toString());
            }
            */
        }

    }
}
