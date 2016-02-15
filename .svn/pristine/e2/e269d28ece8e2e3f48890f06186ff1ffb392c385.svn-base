package cn.fxdata.tv.web;

import cn.fxdata.tv.base.BaseActivity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import cn.fxdata.tv.R;

public class WebActivity extends BaseActivity{

    private static final String TAG = WebActivity.class.getName();
    
    private WebView newsDetailContent;
    
    ImageView loadingView, imgAlpha;
    
    String title;
    
    String url ;
    
    Context context;
    
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        context = this;
        setContentView(R.layout.common_webview_activity_layout);
        initView(arg0);
    }

    protected void initView(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        
        newsDetailContent = (WebView) findViewById(R.id.newsDetailContent);
        newsDetailContent.getSettings().setSupportZoom(true);
        newsDetailContent.getSettings().setBuiltInZoomControls(true);
        newsDetailContent.getSettings().setJavaScriptEnabled(true);
        imgAlpha = (ImageView) findViewById(R.id.webview_alpha);
        
        if (savedInstanceState != null && savedInstanceState.getString(TAG+"Url") != null) {
            title = savedInstanceState.getString(TAG+"Title");
            url = savedInstanceState.getString(TAG+"Url");
        }else{
            url = getIntent().getStringExtra("Url");
            title = getIntent().getStringExtra("Title");
        }
        
        initWebView(url);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        outState.putString(TAG+"Url", url);
        outState.putString(TAG+"Title", title);
        super.onSaveInstanceState(outState);
    }

    void initWebView(final String url){
        newsDetailContent.setWebViewClient(new MyWebViewClient());  
        newsDetailContent.setWebChromeClient(new WebChromeClient());
        newsDetailContent.loadUrl(url);
        
    }
    

    class MyWebViewClient extends WebViewClient{

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
        }
        @Override
        public void onLoadResource(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onLoadResource(view, url);
        }
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // TODO Auto-generated method stub
            cancelShowLoading();
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            if (url.indexOf("app://") != -1) {
                //startActivity(new Intent(CommonWebView.this,RailwayStationSearchTabActivity.class));
                return true;
            }else{
                view.loadUrl(url);       
                return super.shouldOverrideUrlLoading(view, url);
            }
        }
    }
    protected void showLoading() {
        loadingView = (ImageView)findViewById(R.id.webview_loading);
        //loadingView.setImageResource(R.drawable.loading);
        loadingView.setVisibility(View.VISIBLE);
        AnimationDrawable ad = (AnimationDrawable) loadingView.getDrawable();
        ad.start();
    }
    
    protected void cancelShowLoading() {
        //loadingPopupWindow.dismiss();
        loadingView.setVisibility(View.GONE);
    }
    
    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                cancelShowLoading();
            }else {
                showLoading();
            }
            super.onProgressChanged(view, newProgress);
        }
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
          
        if (keyCode == KeyEvent.KEYCODE_BACK
                  && event.getRepeatCount() == 0) {
            
            if (newsDetailContent.canGoBack()) {
                
                newsDetailContent.goBack();
            }else{
                this.finish();
            }
              return true;
          }
          return super.onKeyDown(keyCode, event);
      }
}
