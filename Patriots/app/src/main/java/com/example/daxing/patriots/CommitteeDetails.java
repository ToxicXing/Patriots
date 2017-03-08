package com.example.daxing.patriots;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

import static com.example.daxing.patriots.R.id.chamber_logo;

public class CommitteeDetails extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "CommitteeDetails";
    SharedPreferences sharedPrefs;
    ImageView iv_fav;
    private TextView tv_id, tv_name, tv_chamber, tv_parent, tv_contact, tv_office;
    private ImageView iv_chamber_logo;
    AsyncHttpClient comm_info = new AsyncHttpClient();
    String com_id;
    CommSchema commSchema;
    CommSchema.Committee thisCom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.committee_details);

        setTitle("Committee Info");

        // activate back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        com_id = intent.getStringExtra("committee_id");
        sharedPrefs = getSharedPreferences("favorite_committees", MODE_PRIVATE);

        iv_fav = (ImageView) findViewById(R.id.fav_star);
        iv_fav.setOnClickListener(this);
        if(sharedPrefs.contains(com_id)) {
            iv_fav.setImageResource(R.drawable.fav2);
        } else {
            iv_fav.setImageResource(R.drawable.fav1);
        }

        tv_id = (TextView) findViewById(R.id.content_com_id);
        tv_name = (TextView) findViewById(R.id.content_name);
        tv_chamber = (TextView) findViewById(R.id.content_chamber);
        tv_parent = (TextView) findViewById(R.id.content_parent_committee);
        tv_contact = (TextView) findViewById(R.id.content_contact);
        tv_office = (TextView) findViewById(R.id.content_office);
        iv_chamber_logo = (ImageView) findViewById(chamber_logo);

        String url = "http://104.198.0.197:8080/committees?committee_id="+com_id+"&apikey=ae65eacf997f4d13bd8b518036eb1300";
        comm_info.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder().create();
                commSchema = gson.fromJson(responseString, CommSchema.class);
                Log.i(TAG, "Json got");
                thisCom = commSchema.results.get(0);

                tv_id.setText(thisCom.committee_id);
                tv_name.setText(thisCom.name);
                if (thisCom.chamber.equals("house")) {
                    iv_chamber_logo.setImageResource(R.drawable.h);
                    tv_chamber.setText("House");
                } else if (thisCom.chamber.equals("senate")) {
                    // senate and joint
                    iv_chamber_logo.setImageResource(R.drawable.s);
                    tv_chamber.setText("Senate");
                } else {
                    iv_chamber_logo.setImageResource(R.drawable.s);
                    tv_chamber.setText("Joint");
                }


                if(thisCom.parent_committee_id != null) {
                    tv_parent.setText(thisCom.parent_committee_id);
                } else {
                    tv_parent.setText("N.A");
                }

                if(thisCom.phone != null) {
                    tv_contact.setText(thisCom.phone);
                } else {
                    tv_contact.setText("N.A");
                }

                if(thisCom.office != null) {
                    tv_office.setText(thisCom.office);
                } else {
                    tv_office.setText("N.A");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, "cannot obtain legislators information");
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fav_star : {
                if(sharedPrefs.contains(com_id)) {
                    removeFromFavorite();
                } else {
                    addToFavorite();
                }
                break;
            }
        }
    }
    public void removeFromFavorite() {
        SharedPreferences.Editor editor = getSharedPreferences("favorite_committees", MODE_PRIVATE).edit();
        Gson gson = new GsonBuilder().create();
        editor.remove(com_id);
        editor.commit();
        iv_fav.setImageResource(R.drawable.fav1);
    }

    public void addToFavorite() {
        SharedPreferences.Editor editor = getSharedPreferences("favorite_committees", MODE_PRIVATE).edit();
        Gson gson = new GsonBuilder().create();
        editor.putString(com_id, gson.toJson(thisCom));
        editor.commit();
        iv_fav.setImageResource(R.drawable.fav2);
    }
}
