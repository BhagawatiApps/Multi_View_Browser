package com.bhagawatiapps.multiviewbrowser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.Objects;

public class YoutubeViewerPage extends AppCompatActivity {
    // Declare variables
    ArrayList<String> arrayList = new ArrayList<>();
    GridView gridView;
    TextView ToolbarText;
    String VIDEO_LINK;
    int VIDEO_TAB;
    int CUSTOM_TAB;
    int RefreshTime = 0;
    com.bhagawatiapps.multiviewbrowser.ListAdapter adapter;
    AdRequest adRequest;
    private RewardedAd rewardedAd;
    private boolean rewardedAdLoaded;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_youtube_viewer_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        gridView = findViewById(R.id.gridView);
        ToolbarText = findViewById(R.id.ToolbatText);

        // Get intent from previous activity
        Intent intent = getIntent();

        // Extract data from intent and initialize variables
        CUSTOM_TAB = intent.getIntExtra("custom_tab", 1);
        if (CUSTOM_TAB == 1 || CUSTOM_TAB == 2) {
            VIDEO_LINK = intent.getStringExtra("Vlink");
            VIDEO_TAB = intent.getIntExtra("VTab", 1);
            RefreshTime = intent.getIntExtra("RefreshTime", 75);

            // Populate arrayList with VIDEO_LINK based on VIDEO_TAB
            for (int i = 0; i < VIDEO_TAB; i++) {
                arrayList.add(VIDEO_LINK);
            }
        } else if (CUSTOM_TAB == 3) {
            // Extract individual video links from intent
            String YTvideo1 = intent.getStringExtra("YTvideo1");
            String YTvideo2 = intent.getStringExtra("YTvideo2");
            String YTvideo3 = intent.getStringExtra("YTvideo3");
            String YTvideo4 = intent.getStringExtra("YTvideo4");

            // Add individual video links to arrayList
            arrayList.add(YTvideo1);
            arrayList.add(YTvideo2);
            arrayList.add(YTvideo3);
            arrayList.add(YTvideo4);
        }

        // Initialize adapter with data and set it to gridView
        adapter = new ListAdapter(this, arrayList, CUSTOM_TAB, RefreshTime);
        gridView.setAdapter(adapter);

        // Setting Ad
        AdView adView = findViewById(R.id.adView);
        MobileAds.initialize(this, initializationStatus -> {
        });
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Rewarded Video Ad
        loadRewardedVideoAd();
        // Native Ad
        loadNativeAd();


    }

    private void loadNativeAd() {
        // Load the native ad
        AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.Native_Ad))
                .forNativeAd(nativeAd -> {
                    // Show the ad.
                    if (CUSTOM_TAB <= 4) {
                        @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.native_ad_layout, null);
                        populateNativeAdView(nativeAd, adView);
                        ViewGroup adContainer = findViewById(R.id.ad_container);
                        adContainer.removeAllViews();
                        adContainer.addView(adView);
                    }
                    // Release the native ad.
                    nativeAd.destroy();
                })
                .withAdListener(new com.google.android.gms.ads.AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        // Handle the failure by logging, altering the UI, etc.
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));

        ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());
        ((TextView) Objects.requireNonNull(adView.getBodyView())).setText(nativeAd.getBody());
        ((Button) Objects.requireNonNull(adView.getCallToActionView())).setText(nativeAd.getCallToAction());

        NativeAd.Image icon = nativeAd.getIcon();
        if (icon == null) {
            Objects.requireNonNull(adView.getIconView()).setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }

    private void loadRewardedVideoAd() {
        if (rewardedAdLoaded) {
            return;
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, getString(R.string.Reworded_Ad), adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                rewardedAd = ad;
                rewardedAdLoaded = true;
                setFullScreenContentCallback();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                rewardedAd = null;
                rewardedAdLoaded = false;
            }
        });
    }

    private void setFullScreenContentCallback() {
        if (rewardedAd != null) {
            rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    rewardedAd = null;
                    rewardedAdLoaded = false;
                    loadRewardedVideoAd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    finish();
                }
            });
        }
    }

    private void showRewardedVideoAd() {
        if (rewardedAd != null) {
            rewardedAd.show(this, rewardItem -> {
                // Handle the reward
                finish();
            });
        } else {
            // Ad not loaded yet
            loadRewardedVideoAd();
        }
    }

    @Override
    public void onBackPressed() {
        if (rewardedAdLoaded) {
            showRewardedVideoAd();
        } else {
            super.onBackPressed();
        }
    }
}
