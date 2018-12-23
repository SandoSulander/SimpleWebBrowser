package store.catsocket.simplewebbrowser;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    WebView web;
    EditText browseText;
    ProgressBar progressBar;
    boolean back;
    boolean search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        back = false;
        search = false;

        // Elements
        web = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        browseText = (EditText) findViewById(R.id.browseText);

        // Enter to go
        browseText.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            search(v);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        // Progress bar
        web.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);

                if(newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                }else{
                    progressBar.setVisibility(View.VISIBLE);

                }
            }
        });

        // WebViewClient and get urls
        web.setWebViewClient(new WebViewClient()        {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                Log.d("WebView", "your current url when webpage loading.." + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebView", "your current url when webpage loading.. finish" + url);
                super.onPageFinished(view, url);
                WebAddressIterator wai = WebAddressIterator.getInstance();
                System.out.println("##########################################BACK_VALUE#############################"+back);
                if (back == false && search == true){
                    browseText.setText(url);
                    WebAddress wa = new WebAddress();
                    wa.setUrl(browseText.getText().toString());
                    wai.addWebAddress(wa);
                    wai.setCurrentPage(1);
                } else if(back == false && search == false){
                    browseText.setText(url);
                    WebAddress wa = new WebAddress();
                    wa.setUrl(browseText.getText().toString());
                    wai.addWebAddress(wa);
                    wai.setCurrentPage(1);
                    wai.forwardUrls.clear();
                } else{ browseText.setText(url);

                }
                back = false;
                search = false;
                System.out.println("##########################################INDEX#############################"+wai.currentPage);
                System.out.println("##########################################LIST_SIZE#############################"+wai.urlList.size());
                System.out.println("##########################################URL#############################"+wai.urlList.get(wai.currentPage).getUrl());
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onLoadResource(view, url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("when you click on any interlink on webview that time you got url :-" + url);
                return super.shouldOverrideUrlLoading(view, url);
            }

        });

        // Enable JavaScript
        web.getSettings().setJavaScriptEnabled(true);
    }

    public void search(View v){

        back = false;
        search = true;

        if (browseText.getText().toString().endsWith(".html")) {
            web.loadUrl("file:///android_asset/"+browseText.getText().toString());
            browseText.setText("file:///android_asset/"+browseText.getText().toString());
        }else if (browseText.getText().toString().startsWith("https://") || browseText.getText().toString().startsWith("http://")){
            web.loadUrl(browseText.getText().toString());
           // browseText.setText(browseText.getText().toString());
        } else{
            web.loadUrl("http://"+browseText.getText().toString());
           // browseText.setText("http://"+browseText.getText().toString());
        }

    }

    public void refresh(View v){

        back = true;
        WebAddressIterator wai = WebAddressIterator.getInstance();
        String url = wai.getRefreshAddressFromList();
        web.loadUrl(url);

    }

    public void back(View v){
        WebAddressIterator wai = WebAddressIterator.getInstance();
        if (wai.getForward() == false){
            wai.setForward(true);
            Toast.makeText(this,"Can't go any backward!" , Toast.LENGTH_SHORT).show();
        } else{
            back = true;
            WebAddress wa = wai.getPreviousUrl();
            String url = wa.getUrl();
            System.out.println("##########################################BACK_URL#############################"+url);
            web.loadUrl(url);
            System.out.println("##########################################BACK_VALUE#############################"+back);
        }

    }

    public void forward(View v){

        WebAddressIterator wai = WebAddressIterator.getInstance();
        String url = wai.getForwardUrl();
        if (wai.getForward() == false){
            wai.setForward(true);
            Toast.makeText(this,"Can't go any forward!" , Toast.LENGTH_SHORT).show();
        } else{
            back = true;
            web.loadUrl(url);
        }
    }

    // Execute Javascript methods
    public void executeInitialize(View v){ web.evaluateJavascript("javascript:initialize()", null); }

    public void executeShoutOut(View v){
        web.evaluateJavascript("javascript:shoutOut()", null);
    }
}
