package com.example.daxing.patriots;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.ParseException;

public class LegislatorDetails extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "LegislatorDetails";
    String bioguide_id;
    ImageView iv_avater;
    ImageView iv_chamber_logo;
    ImageView iv_fav,iv_facebook, iv_twitter, iv_website;
    TextView tv_party_text;
    TextView tv_name, tv_email, tv_chamber, tv_contact, tv_start_term, tv_end_term, tv_office, tv_status, tv_state, tv_fax, tv_birthday, tv_progress_text;
    SharedPreferences sharedPrefs;
    AsyncHttpClient legislator_info = new AsyncHttpClient();
    LegiSchema legiSchema;
    private ProgressBar pb_term;
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();
    private int term_start, term_end;
    private LegislatorDetails legislatorDetails;
    LegiSchema.Legislator thisLegislator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legislator_details);

        setTitle("Legislator Info");

        // activate back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent get_bioguide_id = getIntent();
        bioguide_id = get_bioguide_id.getStringExtra("bioguide_id");

        sharedPrefs = getSharedPreferences("favorite_legis", MODE_PRIVATE);

        iv_avater = (ImageView) findViewById(R.id.iv_avater);
        iv_chamber_logo = (ImageView) findViewById(R.id.chamber_logo);

        iv_fav = (ImageView) findViewById(R.id.fav_star);
        if(sharedPrefs.contains(bioguide_id)) {
            Log.i(TAG,"contains this legilastor");
            iv_fav.setImageResource(R.drawable.fav2);
        } else {
            Log.i(TAG,"doens't contain this legilastor");
            iv_fav.setImageResource(R.drawable.fav1);
        }
        iv_fav.setOnClickListener(this);

        iv_facebook = (ImageView) findViewById(R.id.facebook_logo);
        iv_facebook.setOnClickListener(this);
        iv_twitter = (ImageView) findViewById(R.id.twitter_logo);
        iv_twitter.setOnClickListener(this);
        iv_website = (ImageView) findViewById(R.id.website_logo);
        iv_website.setOnClickListener(this);


        tv_party_text = (TextView) findViewById(R.id.party_text);
        tv_name = (TextView) findViewById(R.id.content_name);
        tv_email = (TextView) findViewById(R.id.content_email);
        tv_chamber = (TextView) findViewById(R.id.content_chamber);
        tv_contact = (TextView) findViewById(R.id.content_contact);
        tv_start_term = (TextView) findViewById(R.id.content_start_term);
        tv_end_term = (TextView) findViewById(R.id.content_end_term);
        pb_term = (ProgressBar) findViewById(R.id.content_term);
        tv_progress_text = (TextView) findViewById(R.id.tv_progress_text);
        tv_office = (TextView) findViewById(R.id.content_office);
        tv_state = (TextView) findViewById(R.id.content_state);
        tv_fax = (TextView) findViewById(R.id.content_fax);
        tv_birthday = (TextView) findViewById(R.id.content_birthday);




        String url = "http://104.198.0.197:8080/legislators?bioguide_id=" + bioguide_id;
        legislator_info.get(url, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Gson gson = new GsonBuilder().create();
                legiSchema = gson.fromJson(responseString, LegiSchema.class);
                Log.i(TAG, "Json got");
                thisLegislator = legiSchema.results.get(0);
                String avater_url = "https://theunitedstates.io/images/congress/original/" + thisLegislator.bioguide_id + ".jpg";
                // set avater photo to iv_avater imageview
//                new ImageDownloaderTask(iv_avater).execute(avater_url);
                Picasso.with(getApplicationContext())
                        .load(avater_url)
                        .resize(300, 400)
                        .centerCrop()
                        .into(iv_avater);

                Log.i(TAG, "Image set");

                if(thisLegislator.party.equals("D")) {
//                    iv_chamber_logo.setImageResource(R.drawable.d);
                    Picasso.with(getApplicationContext()).load(R.drawable.d).into(iv_chamber_logo);
                    tv_party_text.setText("Democratic");
                } else {
                    Picasso.with(getApplicationContext()).load(R.drawable.r).into(iv_chamber_logo);
//                    iv_chamber_logo.setImageResource(R.drawable.r);
                    tv_party_text.setText("Republican");
                }

                tv_name.setText(thisLegislator.title + "." + thisLegislator.last_name + "," + thisLegislator.first_name);
                tv_email.setText(thisLegislator.oc_email);
                if (thisLegislator.chamber == "senate") {
                    tv_chamber.setText("Senate");
                } else {
                    tv_chamber.setText("House");
                }

                tv_contact.setText(thisLegislator.phone);

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date term_end_date = formatter.parse(thisLegislator.term_end);
                    tv_end_term.setText(new SimpleDateFormat("MMM dd, yyyy").format(term_end_date));
                    Date term_start_date = formatter.parse(thisLegislator.term_start);
                    tv_start_term.setText(new SimpleDateFormat("MMM dd, yyyy").format(term_start_date));
                    Date birthday_date = formatter.parse(thisLegislator.birthday);
                    tv_birthday.setText(new SimpleDateFormat("MMM dd, yyyy").format(birthday_date));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                Log.i(TAG, "term end is " + thisLegislator.term_end);
                tv_office.setText(thisLegislator.office);
                tv_state.setText(thisLegislator.state);
                tv_fax.setText(thisLegislator.fax);



                mProgressStatus = calculateTerm();
                Log.i(TAG, "progress stauts is " + mProgressStatus);
                mHandler.post(new Runnable() {
                    public void run() {
                        pb_term.setProgress(mProgressStatus);
                    }
                });



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i(TAG, "cannot obtain legislators information");
            }

        });



    }

    public int calculateTerm() {
        int today_int = (int) (new Date().getTime()/1000);
        Log.i(TAG, "today int is " + today_int);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        try {
            term_start = (int) (dateFormat.parse(tv_start_term.getText().toString()).getTime()/1000);
            Log.i(TAG, "term start is " + term_start);
            term_end = (int) (dateFormat.parse(tv_end_term.getText().toString()).getTime()/1000);
            Log.i(TAG, "term end is " + term_end);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        float tmp = (float)(today_int - term_start) / (term_end - term_start);
        tv_progress_text.setText(String.valueOf((int)(tmp * 100)) + "%");
        Log.i(TAG, "tmp is " + tmp);
        return (int) (tmp * 100);
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
            case R.id.facebook_logo: {
                if (thisLegislator.facebook_id == null) {
                    showToast("Website not available!");
                } else {
                    String url = "https://www.facebook.com/" + thisLegislator.facebook_id;
                    openExternalLink(url);
                }
                break;
            }
            case R.id.twitter_logo : {
                if (thisLegislator.twitter_id == null) {
                    showToast("Website not available!");
                } else {
                    String url = "https://www.twitter.com/" + thisLegislator.twitter_id;
                    openExternalLink(url);
                }
                break;
            }
            case R.id.website_logo : {
                if (thisLegislator.website == null) {
                    showToast("Website not available!");
                } else {
                    String url = thisLegislator.website;
                    openExternalLink(url);
                }
                break;
            }
            case R.id.fav_star : {
                if(sharedPrefs.contains(bioguide_id)) {
                    removeFromFavorite();
                } else {
                    addToFavorite();
                }
                break;
            }
        }
    }

    public void openExternalLink(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void removeFromFavorite() {
        SharedPreferences.Editor editor = getSharedPreferences("favorite_legis", MODE_PRIVATE).edit();
        Gson gson = new GsonBuilder().create();
        editor.remove(bioguide_id);
        editor.commit();
        iv_fav.setImageResource(R.drawable.fav1);
    }

    public void addToFavorite() {
        SharedPreferences.Editor editor = getSharedPreferences("favorite_legis", MODE_PRIVATE).edit();
        Gson gson = new GsonBuilder().create();
        editor.putString(bioguide_id, gson.toJson(thisLegislator));
        editor.commit();
        iv_fav.setImageResource(R.drawable.fav2);
    }

    public void showToast(final String msg) {
        Log.d(TAG, "Showing Toast: '" + msg + "'");
        if (legislatorDetails != null) {
            legislatorDetails.runOnUiThread(new Runnable() { // Run the Toast on the
                // Activity UI thread
                @Override
                public void run() {
                    Toast toast = Toast.makeText(LegislatorDetails.this, msg, Toast.LENGTH_LONG);
                    toast.show();
                }
            });
        } else {
            Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
            toast.show();
        }
    }// showToast
}
