package station.quality.air.mini.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener{

    private WebView webView_config;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String curr_url;
    private static final String GRAFANA_URL = "http://localhost:3000";
    private static final String HOMEPAGE_URL = "http://localhost";
    private static final String CONFIGURATION_URL = "http://localhost/configuration.php";
    private static final String ABOUT_URL = "http://localhost/about.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.openNavDrawer, R.string.closeNavDrawer
        );


        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        navigationView.setNavigationItemSelectedListener(this);

         //Change this one day - it's special macro for dev machines localhost
        webView_config = findViewById(R.id.webView_config);
        webView_config.setWebViewClient(new WebViewClient());
        webView_config.getSettings().setJavaScriptEnabled(true);
        setUrl(HOMEPAGE_URL);
        reloadWebsite();
    }

    void setUrl(String url){
        this.curr_url = url;
    }

    void reloadWebsite(){
        webView_config.loadUrl(this.curr_url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && this.webView_config.canGoBack()){
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
        }
        else if (item.getItemId() == R.id.url_menu_1){
            setUrl(CONFIGURATION_URL);
            reloadWebsite();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (item.getItemId() == R.id.url_menu_2){
            setUrl(GRAFANA_URL);
            reloadWebsite();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        else if (item.getItemId() == R.id.url_menu_3){
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