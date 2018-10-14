package j.com.giphysearch.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;


import j.com.giphysearch.entity.Gif;

@Dao
public interface GifDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void writeGifToDB(Gif gif);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void writeGifsToDB(List<Gif> gifs);

    @Query("SELECT * FROM gif_table")
    LiveData<List<Gif>> getAllGifs();

    @Query("SELECT * FROM gif_table WHERE trending__date = :trendingDate")
    LiveData<List<Gif>> getAllTrendingGifs(Date trendingDate);

    @Query("SELECT * FROM gif_table WHERE id = :id")
    LiveData<Gif>getGifByID(String id);

    @Query("DELETE FROM gif_table")
    void deleteAll();

}
