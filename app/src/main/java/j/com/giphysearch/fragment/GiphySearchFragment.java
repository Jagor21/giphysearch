package j.com.giphysearch.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import j.com.giphysearch.R;
import j.com.giphysearch.entity.Gif;
import j.com.giphysearch.ui.FragmentGifImageClickListener;
import j.com.giphysearch.ui.GifAdapter;
import j.com.giphysearch.utils.AppNetworkStatus;
import j.com.giphysearch.viewModel.GifSearchViewModel;

public class GiphySearchFragment extends Fragment implements GifAdapter.ClickListener {

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    @BindView(R.id.last_search_query)
    TextView lastSearchQuery;

    @BindView(R.id.your_last_search_msg)
    TextView yourLastSearchMsg;

    @BindView(R.id.search_et)
    EditText searchEt;

    private GifAdapter adapter;
    private GifSearchViewModel viewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static Context mContext;
    private String searchQuery;
    private FragmentGifImageClickListener listener;

    public void setOnFragmentGifImageClickListener(FragmentGifImageClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_giphy_search, container, false);
        ButterKnife.bind(this, view);

        /*
         * Getting LAST_SEARCH_QUERY
         * from SharedPreferences for updateUIOffline()
         */
        sharedPreferences = getContext().getSharedPreferences("LAST_SEARCH_QUERY", Context.MODE_PRIVATE);
        updateUIOffline();
        configureRecyclerView();
        searchEt.setOnEditorActionListener((TextView.OnEditorActionListener) (v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchQuery = searchEt.getText().toString();
                if (searchQuery.equals("")) {
                    Toast.makeText(mContext, "Please enter a search keyword!", Toast.LENGTH_SHORT).show();
                } else {
                    searchForGifs(searchQuery);
                }
                return true;
            }
            return false;
        });
        setupListenersToHideKeyboard(view.findViewById(R.id.search_parent));
        return view;
    }

    private void configureRecyclerView() {
        recyclerView.setHasFixedSize(true);
        adapter = new GifAdapter(getContext());
        adapter.setOnClickListener(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    //Handling Network connection state
    private void updateUIOffline() {
        mContext = getContext();
        if (!isOnlineCheck()) {
            String lastSearchQueryLocal = sharedPreferences.getString("SEARCH_QUERY", "");
            yourLastSearchMsg.setVisibility(View.VISIBLE);
            lastSearchQuery.setVisibility(View.VISIBLE);
            lastSearchQuery.setText(lastSearchQueryLocal);
        } else {
            yourLastSearchMsg.setVisibility(View.GONE);
            lastSearchQuery.setVisibility(View.GONE);
        }
    }

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isOnlineCheck();
    }

    private void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchEt.getWindowToken(), 0);
    }

    private void searchForGifs(String search) {
        if (isOnlineCheck()) {
            yourLastSearchMsg.setVisibility(View.GONE);
            lastSearchQuery.setVisibility(View.GONE);
            viewModel.getGifsBySearch(search);
            hideKeyboard();
            editor = sharedPreferences.edit();

            editor.putString("SEARCH_QUERY", search);
            editor.apply();
        } else {
            updateUIOffline();
        }
        hideKeyboard();
    }

    /*
     * Creating viewModel,
     * adding observer,
     * and managing UI
     */
    private void configureViewModel() {
        viewModel = ViewModelProviders.of(this).get(GifSearchViewModel.class);
        viewModel.getmGifsSearch().observe(this, mGifsSearch -> {
            if (adapter == null) {
                adapter = new GifAdapter(mContext);
                adapter.setData(mGifsSearch);
                recyclerView.setAdapter(adapter);
                recyclerView.scrollToPosition(0);
            } else {
                adapter.setData(mGifsSearch);
            }
        });
    }

    public void setupListenersToHideKeyboard(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard();
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupListenersToHideKeyboard(innerView);
            }
        }
    }

    @Override
    public void onGifImageClick(Gif gif) {
        viewModel.writeGif(gif);
        listener.onFragmentGifImageClick(gif);
    }
}
