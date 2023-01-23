package station.quality.air.mini.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    private WebView webView_config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView_config = findViewById(R.id.webView_config);
        webView_config.getSettings().setJavaScriptEnabled(true);
        webView_config.loadUrl("http://10.0.2.2"); //Change this one day - it's special macro for dev machines localhost
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && this.webView_config.canGoBack()){
            this.webView_config.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}