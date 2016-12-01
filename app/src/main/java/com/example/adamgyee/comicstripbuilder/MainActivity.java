package com.example.adamgyee.comicstripbuilder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.simplify.ink.InkView;

public class MainActivity extends AppCompatActivity {

    private ImageButton mNewStrip;
    private Button mOnlineRoulette;

/*
    @Override
    public void onBackPressed()
    {

    }
*/

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("i'm here", "me too");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner numArtistSpinner = (Spinner) findViewById(R.id.num_artists_spinner);
        ArrayAdapter<CharSequence> spinner_adapter = ArrayAdapter.createFromResource(this,
                R.array.num_artists_array, android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numArtistSpinner.setAdapter(spinner_adapter);

        // Get permissions
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        mNewStrip = (ImageButton) findViewById(R.id.new_strip_btn);
        mNewStrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Grab the number of artists from spinner
                // TODO: make this not dumb
                int numArtistsInt = 3;
                String numArtists = numArtistSpinner.getSelectedItem().toString();
                switch (numArtists){
                    case "3":
                        numArtistsInt = 3;
                        break;
                    case "4":
                        numArtistsInt = 4;
                        break;
                    case "5":
                        numArtistsInt = 5;
                        break;
                    default:
                        // Shouldn't ever be here
                }

                // Send numArtists to next intent
                Intent nextIntent = new Intent(MainActivity.this, DrawActivity.class);
                nextIntent.putExtra("numArtists", numArtistsInt);
                MainActivity.this.startActivity(nextIntent);
                MainActivity.this.finish();
            }
        });

        mOnlineRoulette = (Button) findViewById(R.id.online_btn);
        mOnlineRoulette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: get internet permissions

                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.INTERNET)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                            Manifest.permission.INTERNET)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.INTERNET},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }

                Intent nextIntent = new Intent(MainActivity.this, OnlineDrawActivity.class);
                MainActivity.this.startActivity(nextIntent);
                MainActivity.this.finish();
            }
        });

    }
}
