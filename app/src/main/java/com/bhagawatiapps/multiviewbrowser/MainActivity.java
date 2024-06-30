package com.bhagawatiapps.multiviewbrowser;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import java.util.ArrayList;

import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity {
    public static int UPDATE_CODE = 100;
    // Declare your UI elements
    DrawerLayout drawerLayout;
    com.google.android.material.navigation.NavigationView NavigationView;
    ImageView NavIcon;
    Toolbar toolbar;
    CardView GetMoreViews, GmvInLoop, DVinDTab, Thankful;
    AppUpdateManager appUpdateManager;

    AdRequest adRequest;

    // Declare variables
    String Vdo = "";
    String YTvideo1 = "";
    String YTvideo2 = "";
    String YTvideo3 = "";
    String YTvideo4 = "";
    int Tno = 0;
    int RefreshTime = 75;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements by finding their IDs
        drawerLayout = findViewById(R.id.DrawerLaout);
        NavigationView = findViewById(R.id.NavigationView);
        NavIcon = findViewById(R.id.NavIcon);
        toolbar = findViewById(R.id.Toolbar);
        GetMoreViews = findViewById(R.id.GetMoreViews);
        GmvInLoop = findViewById(R.id.GmvInLoop);
        Thankful = findViewById(R.id.Thankful);
        DVinDTab = findViewById(R.id.DVinDTab);


        // Set click listener for Navigation Icon (hamburger icon)
        NavIcon.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                // Close Navigation Drawer if it is open
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                // Open Navigation Drawer if it is closed
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Set item click listener for NavigationView
        NavigationView.setNavigationItemSelectedListener(item -> {

            int ID = item.getItemId();

            // Handle NavigationView item clicks
            if (ID == R.id.menu_share) {
                // Handle Share option
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent ShareIntent = new Intent(Intent.ACTION_SEND);
                ShareIntent.setType("text/plain");
                ShareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                String shareMessage = "https://play.google.com/store/apps/details?id=com.bhagawatiapps.multiviewbrowser "+ "\n\n\n";
                ShareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(ShareIntent, "Share with: "));
            } else if (ID == R.id.menu_rate) {
                // Handle Rate option
                drawerLayout.closeDrawer(GravityCompat.START);
                try {
                    Intent RateIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("https://play.google.com/store/apps/details?id=com.bhagawatiapps.multiviewbrowser"));
                    startActivity(RateIntent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.bhagawatiapps.multiviewbrowser")));
                }
            } else if (ID == R.id.menu_update) {
                // Handle Update option
                drawerLayout.closeDrawer(GravityCompat.START);
                try {
                    Intent RateIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("https://play.google.com/store/apps/details?id=com.bhagawatiapps.multiviewbrowser"));
                    startActivity(RateIntent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.bhagawatiapps.multiviewbrowser")));
                }
            } else if (ID == R.id.menu_privacy_policy) {
                // Handle Privacy Policy option
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bhagawatiappsweb.blogspot.com/2024/05/multi-view-browser-app-privacy-policy.html"));
                startActivity(browserIntent);
            }

            return true;
        });

        // Show dialog for GetMoreViews option
        GetMoreViews.setOnClickListener(v -> {

            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.get_video_link_dialog);
            dialog.show();
            EditText VideoLink = dialog.findViewById(R.id.VideoLink);
            Spinner VideoTabNo = dialog.findViewById(R.id.Spiner);
            ImageView PlayVideo = dialog.findViewById(R.id.PlayVideo);
            // Setup Spinner for selecting tab number

            ArrayList<String> TabNo = new ArrayList<>();
            TabNo.add("2 Tab");
            TabNo.add("4 Tab");
            TabNo.add("6 Tab");
            TabNo.add("8 Tab");

            ArrayAdapter<String> arrayTabAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, TabNo);
            VideoTabNo.setAdapter(arrayTabAdapter);
            VideoTabNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Tno = (position + 1) * 2; // Calculate Tno based on position


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Tno = 2;
                }
            });
            // Set click listener for PlayVideo button

            PlayVideo.setOnClickListener(v1 -> {
                if (!VideoLink.getText().toString().isEmpty()) {

                    Vdo = VideoLink.getText().toString();

                    Intent intent = new Intent(MainActivity.this, YoutubeViewerPage.class);
                    intent.putExtra("Vlink", Vdo);
                    intent.putExtra("VTab", Tno);
                    intent.putExtra("RefreshTime", RefreshTime);
                    intent.putExtra("custom_tab", 1);
                    startActivity(intent);
                    dialog.dismiss();
                } else {
                    VideoLink.setBackground(getDrawable(R.drawable.worng_input_color));
                    Toast.makeText(MainActivity.this, "Please enter valid link", Toast.LENGTH_SHORT).show();
                }

            });


        });
        // Show dialog for GmvInLoop option
        GmvInLoop.setOnClickListener(v -> {

            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.loop_video_link_dialog);
            dialog.show();
            EditText VideoLink = dialog.findViewById(R.id.VideoLink);
            EditText VideoLength = dialog.findViewById(R.id.VideoLength);
            Spinner VideoTabNo = dialog.findViewById(R.id.Spiner);
            ImageView PlayVideo = dialog.findViewById(R.id.PlayVideo);
            // Setup Spinner for selecting tab number

            ArrayList<String> TabNo = new ArrayList<>();
            TabNo.add("2 Tab");
            TabNo.add("4 Tab");
            TabNo.add("6 Tab");
            TabNo.add("8 Tab");

            ArrayAdapter<String> arrayTabAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, TabNo);
            VideoTabNo.setAdapter(arrayTabAdapter);
            VideoTabNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Tno = (position + 1) * 2; // Calculate Tno based on position

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Tno = 2;
                }
            });


            // Set click listener for PlayVideo button

            PlayVideo.setOnClickListener(v1 -> {
                if (!VideoLink.getText().toString().isEmpty() && !VideoLength.getText().toString().isEmpty()) {

                    Vdo = VideoLink.getText().toString();
                    RefreshTime = Integer.parseInt(VideoLength.getText().toString());

                    Intent intent = new Intent(MainActivity.this, YoutubeViewerPage.class);
                    intent.putExtra("Vlink", Vdo);
                    intent.putExtra("VTab", Tno);
                    intent.putExtra("RefreshTime", RefreshTime);
                    intent.putExtra("custom_tab", 2);
                    startActivity(intent);
                    dialog.dismiss();
                } else {
                    VideoLink.setBackground(getDrawable(R.drawable.worng_input_color));
                    VideoLength.setBackground(getDrawable(R.drawable.worng_input_color));
                    Toast.makeText(MainActivity.this, "Please enter valid data", Toast.LENGTH_SHORT).show();

                }

            });

        });
        // Show dialog for DVinDTab option

        DVinDTab.setOnClickListener(v -> {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.get_custom_tab_dialog);
            dialog.show();

            EditText VideoLink1 = dialog.findViewById(R.id.VideoLink1);
            EditText VideoLink2 = dialog.findViewById(R.id.VideoLink2);
            EditText VideoLink3 = dialog.findViewById(R.id.VideoLink3);
            EditText VideoLink4 = dialog.findViewById(R.id.VideoLink4);
            ImageView PlayVideo = dialog.findViewById(R.id.PlayVideo);
            // Set click listener for PlayVideo button

            PlayVideo.setOnClickListener(v1 -> {
                if (!VideoLink1.getText().toString().isEmpty() && !VideoLink2.getText().toString().isEmpty() && !VideoLink3.getText().toString().isEmpty() && !VideoLink4.getText().toString().isEmpty()) {

                    YTvideo1 = VideoLink1.getText().toString();
                    YTvideo2 = VideoLink2.getText().toString();
                    YTvideo3 = VideoLink3.getText().toString();
                    YTvideo4 = VideoLink4.getText().toString();

                    Intent intent = new Intent(MainActivity.this, YoutubeViewerPage.class);
                    intent.putExtra("YTvideo1", YTvideo1);
                    intent.putExtra("YTvideo2", YTvideo2);
                    intent.putExtra("YTvideo3", YTvideo3);
                    intent.putExtra("YTvideo4", YTvideo4);
                    intent.putExtra("custom_tab", 3);

                    startActivity(intent);
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Please fill full all details ", Toast.LENGTH_LONG).show();
                }
            });


        });

        // Auto rate dialog code start
        AppRate.with(this).setInstallDays(1).setLaunchTimes(3).setRemindInterval(1).monitor();
        AppRate.showRateDialogIfMeetsConditions(this);
        // Auto rate dialog code end

        // Started InApp Update coding hear
        InApp();

        // Setting Ad
        AdView adView = findViewById(R.id.adView);
        MobileAds.initialize(this, initializationStatus -> {
        });
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


    }

    private void InApp() {
        appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> task = appUpdateManager.getAppUpdateInfo();
        task.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, MainActivity.this, UPDATE_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                    Log.e("InAppUpdate", "Failed to start update flow: " + e.getMessage());
                    // Handle the exception here, such as displaying an error message to the user
                }
            }
        }).addOnFailureListener(e -> {
            // Handle any failure to obtain AppUpdateInfo, such as network errors
            Log.e("InAppUpdate", "Failed to obtain AppUpdateInfo: " + e.getMessage());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_CODE) {

            if (resultCode != RESULT_OK) {

                Toast.makeText(MainActivity.this, "You are now in new version", Toast.LENGTH_SHORT).show();
            }
        }
    }



}