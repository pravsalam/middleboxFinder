package edu.stonybrook.middleboxes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_webview);
////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
//
////        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
////        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
////            }
////        });
//        Log.i("Praveen","Webview will be called");
//        WebView resultWebView = (WebView)findViewById(R.id.resltsWebView);
//        WebSettings webSettings = resultWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        resultWebView.loadUrl("www.google.com");
        WebView webView = new WebView(this);
        setContentView(webView);
        webView.loadUrl("http://www.google.com");
    }

}
