package com.example.adamgyee.comicstripbuilder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.simplify.ink.InkView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OnlineDrawActivity extends AppCompatActivity {

    final private int stripLength = 4;
    private Button mFinished;
    private Button mBlue, mBlack, mWhite, mRed, mYellow, mGreen;

    @Override
    public void onBackPressed() {
        // Don't want users to mess up their masterpieces!
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_draw);

        getOnlineComic();

        getSupportActionBar().setTitle(getResources().getString(R.string.frame_num, 1, stripLength));

        mFinished = (Button) findViewById(R.id.finished_drawing_btn);
        mFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishOnlineFrame();
            }
        });

        setUpButtonListeners();
        mBlack.callOnClick();
    }

    private void getOnlineComic(){

        Log.d("online","getting");
        // Create new RequestQueue (persists(?))
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://10.0.2.2:8080/getComic";

        // Request a json response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    // TODO Parse this to get ID, prevFrame, and current num
                    Log.d("Response: " , response.toString());
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Volley Error ", error.toString());
                }
            });
        // Add the request to the RequestQueue.
        queue.add(jsObjRequest);
    }

    private void finishOnlineFrame(){

        final InkView ink = (InkView) findViewById(R.id.ink);

        // Grab image of canvas
        Bitmap current_drawing = ink.getBitmap(getResources().getColor(android.R.color.white)); // Grab image of canvas

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        current_drawing.compress(Bitmap.CompressFormat.JPEG, 0, baos);
        final byte[] imageBytes = baos.toByteArray();
        final String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        Log.d("lelel:", encodedImage);

        // TODO: make call to save online comic, send encodedImage, ID, and current frame
        RequestQueue queue = Volley.newRequestQueue(this);

        final String url = "http://10.0.2.2:8080/postComic";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("image", encodedImage);
                params.put("domain", "sup");

                return params;
            }
        };
        queue.add(postRequest);

        /*
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("ID", "3");
            jsonBody.put("name", "NAME OF STUDENT");
            jsonBody.put("year", "3rd");
            jsonBody.put("curriculum", "Arts");
            jsonBody.put("birthday", "5/5/1993");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("attempting to post", "fingers crossed");
        new JsonObjectRequest(url, jsonBody, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {

                // TODO Parse this to get ID, prevFrame, and current num
                Log.d("Response: " , response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley Error ", error.toString());
            }
        });
        */

    }

    private void setPrevious(Bitmap current_drawing){
        // Show previous bitmap
        ImageView imageView = (ImageView) findViewById(R.id.preview);
        imageView.setImageBitmap(current_drawing);
    }

    private void clearCanvas(){
        final InkView ink = (InkView) findViewById(R.id.ink);
        ink.clear();
    }

    private void loadPhoto(ImageView imageView, int width, int height) {

        ImageView tempImageView = imageView;

        AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.popup_instructions,
                (ViewGroup) findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        image.setImageDrawable(tempImageView.getDrawable());
        imageDialog.setView(layout);
        imageDialog.setPositiveButton(getString(R.string.action_done), new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        imageDialog.create();
        imageDialog.show();
    }

    private void setUpButtonListeners(){

        final InkView ink = (InkView) findViewById(R.id.ink);

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
}
