package com.example.adamgyee.comicstripbuilder;

import android.content.Intent;
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

    @Override
    public void onBackPressed()
    {

    }

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
            }
        });

    }
}
