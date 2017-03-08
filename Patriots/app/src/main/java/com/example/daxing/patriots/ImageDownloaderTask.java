package com.example.daxing.patriots;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import cz.msebera.android.httpclient.HttpStatus;

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private WeakReference<ImageView> imageViewReference = null;
    public ImageDownloaderTask(){}
    public ImageDownloaderTask(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return downloadBitmap(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.logo);
                    imageView.setImageDrawable(placeholder);
//                    Log.i("ImageDownlaoder", "avater set");
                }
            }
        }
    }

    public Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
//            final BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            options.inSampleSize = 2;
//            options.inJustDecodeBounds = false;
//            options.inTempStorage = new byte[16 * 1024];

            InputStream inputStream = urlConnection.getInputStream();
//            Log.i("DownloaderAsyncTask","getInputStream from " + url);
            if (inputStream != null) {
//                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 96, 73, false);
//                Log.i("DownloaderAsyncTask", "rescale bitmap");
//                return resizedBitmap;

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
