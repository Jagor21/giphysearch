package j.com.giphysearch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.com.giphysearch.fragment.GiphySearchFragment;
import j.com.giphysearch.fragment.GiphyTrendingFragment;
import j.com.giphysearch.service.MusicService;
import j.com.giphysearch.ui.PagerAdapter;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, MusicService.class));
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initViewPager();
        isStoragePermissionGranted();
    }

    public boolean isStoragePermissionGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission is not granted");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        return true;
    }

    private void initViewPager() {
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new GiphyTrendingFragment(), "Trending");
        pagerAdapter.addFragment(new GiphySearchFragment(), "Search");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, MusicService.class));
    }



    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, MusicService.class));
    }
}
