package j.com.giphysearch.database;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import j.com.giphysearch.entity.Gif;

@Database(entities = {Gif.class}, version = 1)
@TypeConverters({MyConverters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase mInstance;
    public abstract GifDAO gifDAO();

    public static AppDatabase getInstance(final Context context) {
        if (mInstance == null) {
            synchronized (AppDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "gif_database")
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return mInstance;
    }
}
