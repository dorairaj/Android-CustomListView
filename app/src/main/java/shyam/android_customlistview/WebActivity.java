package shyam.android_customlistview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * The class WebActivity provides users with means to do in app-browsing.
 * It is invoked when a click is registered on list view, and intent is called from MainActivity
 */

public class WebActivity extends AppCompatActivity {
WebView webView;
Toast toast;
String loadToastText= "Loading..Please wait..";

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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack())
        {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    private class CustomWebViewClient extends WebViewClient
    {
        //make all links open inside app.
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            // provide feedback for user by toast.
            toast= Toast.makeText(getApplicationContext(),loadToastText,Toast.LENGTH_SHORT);
            return false;
        }

    }
}
