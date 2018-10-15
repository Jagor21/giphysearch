package j.com.giphysearch.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.com.giphysearch.R;
import j.com.giphysearch.entity.Gif;
import j.com.giphysearch.ui.GifAdapter;
import j.com.giphysearch.utils.AppNetworkStatus;
import j.com.giphysearch.viewModel.GifTrendingViewModel;

public class GiphyTrendingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, GifAdapter.ClickListener {

    public static final String TRENDING_FRAGMENT = "GiphyTrendingFragment";

    @BindView(R.id.trending_rv)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private GifAdapter adapter;
    private GifTrendingViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TRENDING_FRAGMENT, "onActivityCreated: ");
        configureViewModelForTrending();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_giphy_trending, container, false);
        ButterKnife.bind(this, view);
        configureRecyclerView();
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    /*
     * Creating viewModel,
     * adding observer,
     * and managing UI
     */
    private void configureViewModelForTrending() {
        viewModel = ViewModelProviders.of(this).get(GifTrendingViewModel.class);
        viewModel.getTrendingGifs().observe(this, mGifsTrending -> {
            if (adapter == null) {
                adapter = new GifAdapter(getContext());
                adapter.setOnClickListener(this);
                adapter.setData(mGifsTrending);
                recyclerView.setAdapter(adapter);
                recyclerView.scrollToPosition(0);
            } else {
                adapter.setData(mGifsTrending);
                recyclerView.scrollToPosition(0);
            }
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void configureRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new GifAdapter(getContext());
        adapter.setOnClickListener(this);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    //Here I'm checking Network connection state when user swipes to current Fragment
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isOnlineCheck();
            hideKeyboard(recyclerView);
        }
    }

    //Handling Network connection state
    private boolean isOnlineCheck() {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        boolean isOnline = AppNetworkStatus.getInstance(appCompatActivity).isOnline();
        if (appCompatActivity != null) {
            if (!isOnline) {
                Toast.makeText(appCompatActivity, "Network error!\nPlease check your connection!", Toast.LENGTH_SHORT).show();
            }
        }
        return isOnline;
    }

    @Override
    public void onRefresh() {
        if (isOnlineCheck()) {
            viewModel.getTrendingGifs();
        }
    }

    private void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onGifImageClick(Gif gif) {
        viewModel.writeGif(gif);
    }
}

