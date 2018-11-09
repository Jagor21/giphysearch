package j.com.giphysearch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.com.giphysearch.entity.Gif;
import j.com.giphysearch.fragment.GifFragment;
import j.com.giphysearch.fragment.GiphySearchFragment;
import j.com.giphysearch.fragment.GiphyTrendingFragment;
import j.com.giphysearch.service.MusicService;
import j.com.giphysearch.ui.FragmentGifImageClickListener;
import j.com.giphysearch.ui.PagerAdapter;

public class MainActivity extends AppCompatActivity implements FragmentGifImageClickListener, GifFragment.GifFragmentListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.gif_fragment_container)
    FrameLayout gifFragmentContainer;

    boolean gifFragmentIsShown = false;

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
        GiphyTrendingFragment giphyTrendingFragment = new GiphyTrendingFragment();
        giphyTrendingFragment.setOnFragmentGifImageClickListener(this);
        GiphySearchFragment giphySearchFragment = new GiphySearchFragment();
        giphySearchFragment.setOnFragmentGifImageClickListener(this);
        pagerAdapter.addFragment(giphyTrendingFragment, "Trending");
        pagerAdapter.addFragment(giphySearchFragment, "Search");
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

    @Override
    public void onFragmentGifImageClick(Gif gif) {
        Bundle bundle = new Bundle();
        bundle.putString("gif_id", gif.getId());
        bundle.putString("gif_title", gif.getTitle());
        viewPager.setVisibility(View.INVISIBLE);
        gifFragmentContainer.setVisibility(View.VISIBLE);
        gifFragmentIsShown = true;
        GifFragment gifFragment = new GifFragment();
        gifFragment.setGifFragmentListener(this);
        gifFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).addToBackStack("GifFragment").replace(R.id.gif_fragment_container, gifFragment).commit();
    }

    @Override
    public void onBackPressed() {

        if (gifFragmentIsShown) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            fragmentTransaction.remove(getSupportFragmentManager().findFragmentById(R.id.gif_fragment_container));
            fragmentTransaction.commit();
            viewPager.setVisibility(View.VISIBLE);
            gifFragmentIsShown = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSwipeDownListener() {
        onBackPressed();
    }
}
