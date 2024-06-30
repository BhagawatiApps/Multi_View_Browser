package com.bhagawatiapps.multiviewbrowser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<String> arrayList;
    private final int CUSTOM_TAB;
    private final int RefreshTime;

    ListAdapter(Context context, ArrayList<String> arrayList, int CUSTOM_TAB, int RefreshTime) {
        this.arrayList = arrayList;
        this.context = context;
        this.CUSTOM_TAB = CUSTOM_TAB;
        this.RefreshTime = RefreshTime;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetJavaScriptEnabled", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.web_view_demo, null);
        }

        WebView webView = convertView.findViewById(R.id.WebItem);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(String.valueOf(arrayList.get(position)));
        webView.setWebViewClient(new WebViewClient());

        // Reload the WebView periodically based on CUSTOM_TAB and RefreshTime
        if (CUSTOM_TAB == 1) {
            // Reload every RefreshTime seconds
            reloadWebViewPeriodically(webView, RefreshTime);
        } else if (CUSTOM_TAB == 2) {
            // Reload every RefreshTime minutes
            reloadWebViewPeriodically(webView, RefreshTime * 60);
        }

        return convertView;
    }

    // Method to reload the WebView periodically
    private void reloadWebViewPeriodically(final WebView webView, int RefreshTime) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.reload();
                // Schedule the next reload after RefreshTime seconds
                handler.postDelayed(this, RefreshTime * 1000L);
            }
        }, RefreshTime * 1000L); // Start reloading after RefreshTime seconds
    }
}
