package j.com.giphysearch.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import j.com.giphysearch.database.AppRepository;
import j.com.giphysearch.entity.Gif;

public class GifSearchViewModel extends AndroidViewModel {

    private AppRepository repository;
    private LiveData<List<Gif>> mGifsSearch;


    public GifSearchViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        mGifsSearch = repository.getGifs();
    }

    public LiveData<List<Gif>> getmGifsSearch() {
        return mGifsSearch;
    }

    public void getGifsBySearch(String s) {
        repository.getGifsBySearch(s);
    }


    public void writeGif(Gif gif){
        repository.writeGif(gif);
    }
}
