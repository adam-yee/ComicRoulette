package com.example.adamgyee.comicstripbuilder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
import java.lang.reflect.Array;
import java.util.ArrayList;

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

        // Get permissions
        if (ContextCompat.checkSelfPermission(FinalStrip.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("checked","have permisson");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(FinalStrip.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(FinalStrip.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        // Show download option in ActionBar
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveComicToGallery();
            return true;
        } else if (id == R.id.action_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            FinalStrip.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean saveComicToGallery(){
        // TODO: implement save

        Toast.makeText(getApplicationContext(), "Saving comic to gallery", Toast.LENGTH_SHORT).show();
        LinearLayout layout = (LinearLayout)findViewById(R.id.viewing_panel);

        // combine all bitmaps into a single strip to save
        Bitmap combined = Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < layout.getChildCount(); i++) {
            ImageView v = (ImageView) layout.getChildAt(i);
            if (((BitmapDrawable)v.getDrawable()).getBitmap() != null && !((BitmapDrawable)v.getDrawable()).getBitmap().isRecycled()) {

                // Add border around images, then combine them
                Bitmap bitmap = ((BitmapDrawable)v.getDrawable()).getBitmap();
                RectF targetRect = new RectF(0, 0, bitmap.getWidth()+5, bitmap.getHeight()+5);
                Bitmap dest = Bitmap.createBitmap(bitmap.getWidth()+20, bitmap.getHeight()+20, bitmap.getConfig());
                Canvas canvas = new Canvas(dest);
                canvas.drawColor(Color.BLACK);
                canvas.drawBitmap(bitmap, null, targetRect, null);
                combined = combineImages(combined, dest);

            }
        }

        // Save to local gallery
        MediaStore.Images.Media.insertImage(getContentResolver(), combined, "ComicRoulette Image", "Created with ComicRoulette");
        return true;
    }

    public Bitmap combineImages(Bitmap c, Bitmap s) {
        Bitmap cs = null;
        int width, height = 0;

        width = s.getWidth();
        height = c.getHeight()+s.getHeight();

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        comboImage.drawBitmap(c, 0f, 0f, null);
        comboImage.drawBitmap(s, 0f, c.getHeight(), null);
        return cs;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        FinalStrip.this.finish();
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
            v.setImageBitmap(null);
        }
        FinalStrip.this.finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_strip);

        mViewingPanel = (LinearLayout) findViewById(R.id.viewing_panel);

        Intent intent = getIntent();
        mNumArtists = intent.getIntExtra("numArtists", 3);

        // Retrieve bitmaps from file to display full strip
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
        }
    }
}
