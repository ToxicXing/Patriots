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

import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class BillDetails extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "BillDetails";
    SharedPreferences sharedPrefs;
    ImageView iv_star;
    TextView tv_bill_id, tv_title, tv_bill_type, tv_sponsor, tv_chamber, tv_status, tv_introduced_on, tv_congress_url, tv_version_status, tv_bill_url;
    AsyncHttpClient bill_info = new AsyncHttpClient();
    BillSchema billSchema;
    BillSchema.Bill thisBill;
    String bill_id, bill_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_details);
        // activate back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Bill Info");

        Intent get_bioguide_id = getIntent();
        bill_id = get_bioguide_id.getStringExtra("bill_id");
        bill_status = get_bioguide_id.getStringExtra("bill_status");

        sharedPrefs = getSharedPreferences("favorite_bills", MODE_PRIVATE);

        iv_star = (ImageView) findViewById(R.id.fav_star);
        iv_star.setOnClickListener(this);
        if(sharedPrefs.contains(bill_id)) {
            iv_star.setImageResource(R.drawable.fav2);
        } else {
            iv_star.setImageResource(R.drawable.fav1);
        }
        tv_bill_id = (TextView) findViewById(R.id.content_bill_id);
        tv_title = (TextView) findViewById(R.id.content_title);
        tv_bill_type = (TextView) findViewById(R.id.content_bill_type);
        tv_sponsor = (TextView) findViewById(R.id.content_sponsor);
        tv_chamber = (TextView) findViewById(R.id.content_chamber);
        tv_status = (TextView) findViewById(R.id.content_status);
        tv_introduced_on = (TextView) findViewById(R.id.content_introduced_on);
        tv_congress_url = (TextView) findViewById(R.id.content_congress_url);
        tv_version_status = (TextView) findViewById(R.id.content_version_status);
        tv_bill_url = (TextView) findViewById(R.id.content_bill_url);

        String url = "http://104.198.0.197:8080/bills?bill_id=" + bill_id + "&apikey=ae65eacf997f4d13bd8b518036eb1300&per_page=all";
        bill_info.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder().create();
                billSchema = gson.fromJson(responseString, BillSchema.class);
                Log.i(TAG, "Json got");
                thisBill = billSchema.results.get(0);


                tv_bill_id.setText(thisBill.bill_id.toUpperCase());
                tv_title.setText(thisBill.official_title);
                tv_bill_type.setText(thisBill.bill_type.toUpperCase());
                tv_sponsor.setText(thisBill.sponsor.title + "." + thisBill.sponsor.last_name + ", " + thisBill.sponsor.first_name);
                if (thisBill.chamber == "senate") {
                    tv_chamber.setText("Senate");
                } else {
                    tv_chamber.setText("House");
                }

                if (bill_status.equals("new")) {
                    tv_status.setText("New");
                } else {
                    tv_status.setText("Active");
                }

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date introduced_on_date = formatter.parse(thisBill.introduced_on);
                    tv_introduced_on.setText(new SimpleDateFormat("MMM dd, yyyy").format(introduced_on_date));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                tv_congress_url.setText(thisBill.urls.congress);

                if (thisBill.last_version!=null && thisBill.last_version.version_name!=null) {
                    tv_version_status.setText(thisBill.last_version.version_name);
                } else {
                    tv_version_status.setText("None");
                }

                if (thisBill.last_version!=null && thisBill.last_version.version_name!=null) {
                    tv_bill_url.setText(thisBill.last_version.urls.pdf);
                } else {
                    tv_bill_url.setText("None");
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
                if(sharedPrefs.contains(bill_id)) {
                    removeFromFavorite();
                } else {
                    addToFavorite();
                }
                break;
            }
        }
    }
    public void removeFromFavorite() {
        SharedPreferences.Editor editor = getSharedPreferences("favorite_bills", MODE_PRIVATE).edit();
        Gson gson = new GsonBuilder().create();
        editor.remove(bill_id);
        editor.commit();
        iv_star.setImageResource(R.drawable.fav1);
    }

    public void addToFavorite() {
        SharedPreferences.Editor editor = getSharedPreferences("favorite_bills", MODE_PRIVATE).edit();
        Gson gson = new GsonBuilder().create();
        editor.putString(bill_id, gson.toJson(thisBill));
        editor.commit();
        iv_star.setImageResource(R.drawable.fav2);
    }
}
