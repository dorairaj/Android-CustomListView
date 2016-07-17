package shyam.android_customlistview;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {
WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent =getIntent();
        String url=intent.getStringExtra("url");
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new CustomWebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);

    }
    private class CustomWebViewClient extends WebViewClient {
        //make all links open inside app.
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            //Todo - return toast here network request feedback?
            return true;
        }
    }
}
