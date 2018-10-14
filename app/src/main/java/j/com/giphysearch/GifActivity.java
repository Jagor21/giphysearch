package j.com.giphysearch;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.com.giphysearch.entity.Gif;
import j.com.giphysearch.viewModel.SingleGifViewModel;

public class GifActivity extends AppCompatActivity {

    @BindView(R.id.current_gif)
    ImageView imageView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private SingleGifViewModel viewModel;
    private Gif mGif;
    private String gifId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        ButterKnife.bind(this);
        configureToolbar();
        gifId = getIntent().getExtras().getString("gif_id");
        configureViewModel();
    }

    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this).get(SingleGifViewModel.class);
        viewModel.getGif(gifId).observe(this, gif -> {
            mGif = gif;
            Glide.with(this).asGif().load(gif.getImages().getFixed_height().getUrl()).into(imageView);
            setTitle(gif.getTitle());
        });
    }

    private void configureToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
