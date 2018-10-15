package j.com.giphysearch;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.com.giphysearch.entity.Gif;
import j.com.giphysearch.utils.CustomGestureListener;
import j.com.giphysearch.viewModel.SingleGifViewModel;

public class GifActivity extends AppCompatActivity {

    @BindView(R.id.current_gif)
    ImageView imageView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.parent)
    ConstraintLayout mParent;

    private SingleGifViewModel viewModel;
    private Gif mGif;
    private String gifId;
    private GestureDetector mGestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        ButterKnife.bind(this);
        configureToolbar();
        gifId = getIntent().getExtras().getString("gif_id");
        configuringGestureDetector();
        mParent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return !mGestureDetector.onTouchEvent(event);
            }
        });
        configureViewModel();
    }



    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this).get(SingleGifViewModel.class);
        viewModel.getGif(gifId).observe(this, gif -> {
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
            onBackPressed();
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void configuringGestureDetector() {
        mGestureDetector = new GestureDetector(this, new CustomGestureListener(mParent) {
            @Override
            public boolean onSwipeRight() {
                return false;
            }

            @Override
            public boolean onSwipeLeft() {
                return false;
            }

            @Override
            public boolean onSwipeUp() {
                return false;
            }

            @Override
            public boolean onSwipeDown() {
                onBackPressed();
                return false;
            }

            @Override
            public boolean onTouch() {
                return false;
            }
        });
    }

}
