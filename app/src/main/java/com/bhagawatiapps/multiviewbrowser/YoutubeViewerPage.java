package com.bhagawatiapps.multiviewbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class YoutubeViewerPage extends AppCompatActivity {
    // Declare variables
    ArrayList<String> arrayList = new ArrayList<>();
    GridView gridView;
    TextView ToolbarText;
    String VIDEO_LINK;
    int VIDEO_TAB;
    int CUSTOM_TAB;
    int RefreshTime = 0;
    ListAdapter adapter;

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
        adapter = new ListAdapter(getApplicationContext(), arrayList, CUSTOM_TAB, RefreshTime);
        gridView.setAdapter(adapter);
    }
}