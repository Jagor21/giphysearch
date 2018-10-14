package j.com.giphysearch.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;

import j.com.giphysearch.entity.GifImage;

/*
 * Here I'm creating methods
 * to convert GifImage and Date objects
 * to String and backward.
 * Using in AppDatabase
 */
public class MyConverters {

    @TypeConverter
    public static String fromGifImageToJson(GifImage gifImage) {
        if (gifImage == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<GifImage>() {
        }.getType();
        String json = gson.toJson(gifImage, type);
        return json;
    }

    @TypeConverter
    public static GifImage fromJsonToGifImage(String json) {
        if (json == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<GifImage>() {
        }.getType();
        GifImage gifImage = gson.fromJson(json, type);
        return gifImage;
    }

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
