package j.com.giphysearch.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.com.giphysearch.R;
import j.com.giphysearch.entity.Gif;
import j.com.giphysearch.utils.CustomGestureListener;
import j.com.giphysearch.utils.GifHelper;
import j.com.giphysearch.viewModel.SingleGifViewModel;

public class GifFragment extends Fragment {

    private static final String TAG = "GifFragment";

    @BindView(R.id.current_gif)
    ImageView imageView;

    @BindView(R.id.f_toolbar)
    Toolbar toolbar;

    @BindView(R.id.parent)
    ConstraintLayout mParent;

    private SingleGifViewModel viewModel;
    private Gif mGif;
    private String gifId;
    private GestureDetector mGestureDetector;
    private GifFragmentListener listener;
    private GifHelper gifHelper;

    public void setGifFragmentListener(GifFragmentListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gif, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        gifId = this.getArguments().getString("gif_id");
        configureToolbar();
        configureViewModel();
        configuringGestureDetector();
        gifHelper = new GifHelper(getContext(), mGif);
        return view;
    }

    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this).get(SingleGifViewModel.class);
        viewModel.getGif(gifId).observe(this, gif -> {
            Glide.with(this).asGif().load(gif.getImages().getFixed_height().getUrl()).into(imageView);
            gifHelper = new GifHelper(getContext(), gif);
        });
    }


    private void configureToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.gif_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_save:
                gifHelper.startSavingGif(false);
//                Toast.makeText(getActivity(), "Saving...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_share:
                gifHelper.startSavingGif(true);
//                Toast.makeText(getActivity(), "Sharing...", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void configuringGestureDetector() {
        mGestureDetector = new GestureDetector(getContext(), new CustomGestureListener(mParent) {

            @Override
            public boolean onSwipeUp() {
                return false;
            }

            @Override
            public boolean onSwipeDown() {
                listener.onSwipeDownListener();
                return false;
            }

            @Override
            public boolean onTouch() {
                return false;
            }
        });
    }


    public interface GifFragmentListener {
        void onSwipeDownListener();
    }
}
