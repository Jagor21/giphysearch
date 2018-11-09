package j.com.giphysearch.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import j.com.giphysearch.api.GiphyApi;
import j.com.giphysearch.api.GiphyClient;
import j.com.giphysearch.entity.Gif;
import j.com.giphysearch.entity.GiphyData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppRepository {

    private GifDAO mGifDAO;
    private LiveData<List<Gif>> mGifs;
    private Executor executor;
    private static final String apiKey = "P0nEDDOhrwPIvVOJDx4vgJKbdEKPww3F";

    //MutableLiveData for GiphyTrendingViewModel
    MutableLiveData<List<Gif>> mutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Gif>> gifMutableLiveData = new MutableLiveData<>();


    public AppRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application.getApplicationContext());
        mGifDAO = database.gifDAO();
        mGifs = mGifDAO.getAllGifs();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Gif>> getGifs() {
        return this.mGifs;
    }

    public void getGifsBySearch(String s) {
        Call<GiphyData> callForSearch = GiphyClient.getInstance().create(GiphyApi.class).searchForGifs(s, apiKey);
        callForSearch.enqueue(new Callback<GiphyData>() {
            @Override
            public void onResponse(Call<GiphyData> call, Response<GiphyData> response) {
                if (response.isSuccessful()) {
                    //Refreshing database and writing gifs from the response
                    executor.execute(() -> {
                        mGifDAO.deleteAll();
                        mGifDAO.writeGifsToDB(response.body().getData());
                    });
                }
            }

            @Override
            public void onFailure(Call<GiphyData> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    public LiveData<List<Gif>> getTrendingGifs() {
        Call<GiphyData> callForTrending = GiphyClient.getInstance().create(GiphyApi.class).getTrendingGifs(apiKey);
        callForTrending.enqueue(new Callback<GiphyData>() {
            @Override
            public void onResponse(Call<GiphyData> call, Response<GiphyData> response) {
                if (response.isSuccessful()) {
                    //Setting the value from the response
                    mutableLiveData.setValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<GiphyData> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<Gif> getGifById(String id) {
        return mGifDAO.getGifByID(id);
    }

    public void writeGif(Gif gif) {
        executor.execute(() -> {
            mGifDAO.writeGifToDB(gif);
        });
    }
}
