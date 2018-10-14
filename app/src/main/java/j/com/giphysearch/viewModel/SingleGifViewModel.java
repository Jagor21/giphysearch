package j.com.giphysearch.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import j.com.giphysearch.database.AppRepository;
import j.com.giphysearch.entity.Gif;

public class SingleGifViewModel extends AndroidViewModel {

    private LiveData<Gif> mGif;
    private AppRepository repository;
    private String mID;


    public SingleGifViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public LiveData<Gif> getGif(String id){
        return repository.getGifById(id);
    }
}
