package com.shalom.itai.myglobalphotos;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.File;

/**
 * Proudly written by Itai on 13/10/2018.
 * FullScreenActivity - Shows image on full screen
 */


public class FullScreenActivity extends AppCompatActivity {
    private BottomNavigationView navigation;
    private boolean mVisible;
    private RelativeLayout container;
    private GpsPhoto gpsPhoto;

    public static final String GPS_PHOTO_DATA = "GPS_PHOTO_DATA";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.show_map:
                    if (gpsPhoto.hasGpsData()) {
                        Intent intent = new Intent(FullScreenActivity.this, MapsActivity.class);
                        intent.putExtra(FullScreenActivity.GPS_PHOTO_DATA, gpsPhoto);
                        FullScreenActivity.this.startActivity(intent);
                    } else {
                        Toast.makeText(FullScreenActivity.this, "No Gps data available", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                case R.id.navigation_delete:
                    delete(gpsPhoto.getPath());
                    FullScreenActivity.super.onBackPressed();
                    finish();
                    return true;
                case R.id.navigation_share:
                    share(gpsPhoto.getPath());
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mVisible = true;

        setContentView(R.layout.activity_full_screen_memory);
        container = (RelativeLayout) findViewById(R.id.container);

        container.setBackgroundColor(Color.parseColor("#000000"));
        ImageView imageView = (ImageView) findViewById(R.id.image);

        gpsPhoto = getIntent().getParcelableExtra(GPS_PHOTO_DATA);

        Bitmap bmp = BitmapFactory.decodeFile(gpsPhoto.getPath());
        imageView.setImageBitmap(bmp);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        photoLogic(navigation.getMenu().getItem(0));

        toggle();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
    }

    /**
     * Check if the photo has a GPS data, if there's no data, it greys the option on the map icon
     *
     * @param map - The Icon the map on the navigation bar
     */
    private void photoLogic(MenuItem map) {
        if (!gpsPhoto.hasGpsData()) {
            map.getIcon().setAlpha(50);
            SpannableString s = new SpannableString(map.getTitle());
            s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, s.length(), 0);
            map.setTitle(s);
        } else {
            map.getIcon().setAlpha(255);
            SpannableString s = new SpannableString(map.getTitle());
            s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
            map.setTitle(s);
        }
    }

    /**
     * Shows or hide the navigation bar
     */
    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    /**
     * hides the navigation bar
     */
    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        navigation.setVisibility(View.INVISIBLE);
        mVisible = false;
    }

    /**
     * shows the navigation bar
     */
    private void show() {
        mVisible = true;
        navigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * Deletes the photo from the device
     */
    private void delete(String pathToFile) {
        File file = new File(pathToFile);
        if (file.delete()) {
            Toast.makeText(this.getApplicationContext(), "Deleted ", Toast.LENGTH_SHORT).show();
        } else {
            Snackbar.make(container, "Couldn't delete it", Snackbar.LENGTH_SHORT).show();
        }

    }

    /**
     * share the image to other applications
     */
    private void share(String pathToFile) {
        Uri uriToImage = Uri.parse(pathToFile);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Share Image");
        Intent result = Intent.createChooser(shareIntent, getResources().getText(R.string.send_to));
        startActivity(result);
    }
}
