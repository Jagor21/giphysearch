package j.com.giphysearch.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import j.com.giphysearch.database.AppRepository;
import j.com.giphysearch.entity.Gif;

public class GifTrendingViewModel extends AndroidViewModel {

    private AppRepository repository;
    private LiveData<List<Gif>> trendingGifs;

    public GifTrendingViewModel(@NonNull Application application) {
        super(application);
        this.repository = new AppRepository(application);
        this.trendingGifs = repository.getTrendingGifs();
    }

    public LiveData<List<Gif>> getTrendingGifs(){
        return repository.getTrendingGifs();
    }

    public LiveData<List<Gif>> callForTrendingGifs(){
        LiveData<List<Gif>> newTrendingGifs = repository.getTrendingGifs();
       return newTrendingGifs;
    }

    public void writeGif(Gif gif){
        repository.writeGif(gif);
    }
}
