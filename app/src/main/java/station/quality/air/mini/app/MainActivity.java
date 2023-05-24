package station.quality.air.mini.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final String SAVE_FILENAME = "mini_air_quality_data.csv";
    private WebView webView_config;
    private DrawerLayout drawerLayout;
    private String curr_url;
    private static final String GRAFANA_URL = "http://192.168.88.1:3000";
    private static final String HOMEPAGE_URL = "http://192.168.88.1";
    private static final String CONFIGURATION_URL = "http://192.168.88.1/configuration.php";
    private static final String ABOUT_URL = "http://192.168.88.1/about.php";
    private JavaScriptInterface javaInterface;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        drawerLayout = findViewById(R.id.drawerlayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.openNavDrawer, R.string.closeNavDrawer
        );

        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        navigationView.setNavigationItemSelectedListener(this);

        webView_config = findViewById(R.id.webView_config);
        webView_config.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView_config.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        this.javaInterface = new JavaScriptInterface(this);
        webView_config.addJavascriptInterface(javaInterface, "Android");
        webView_config.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            DownloadManager.Request request;
            try {
                request = new DownloadManager.Request(
                        Uri.parse(url));
            } catch (java.lang.IllegalArgumentException exc) {
                ContentResolver contentResolver = this.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Downloads.DISPLAY_NAME, SAVE_FILENAME);
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
                Uri collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                javaInterface.path = contentResolver.insert(collection, contentValues);
                webView_config.loadUrl(JavaScriptInterface.getBase64StringFromBlobUrl(url, mimetype));
                return;
            }
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            dm.enqueue(request);
            Toast.makeText(this, "Download started!", Toast.LENGTH_SHORT).show();
        });
        
        setUrl(HOMEPAGE_URL);
        reloadWebsite();
    }

    void setUrl(String url) {
        this.curr_url = url;
    }

    void reloadWebsite() {
        webView_config.loadUrl(this.curr_url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && this.webView_config.canGoBack()) {
            this.webView_config.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.url_menu_0) {
            setUrl(HOMEPAGE_URL);
            reloadWebsite();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == R.id.url_menu_1) {
            setUrl(CONFIGURATION_URL);
            reloadWebsite();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == R.id.url_menu_2) {
            setUrl(GRAFANA_URL);
            reloadWebsite();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (item.getItemId() == R.id.url_menu_3) {
            setUrl(ABOUT_URL);
            reloadWebsite();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
