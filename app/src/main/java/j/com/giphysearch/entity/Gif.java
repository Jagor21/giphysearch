package j.com.giphysearch.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.util.Date;

import j.com.giphysearch.database.MyConverters;


@Entity(tableName = "gif_table")
public class Gif {

    private String type;
    @PrimaryKey
    @NonNull
    private String id;
    private String url;
    @TypeConverters({MyConverters.class})
    @ColumnInfo(name = "images")
    private GifImage images;
    private String title;
    @TypeConverters({MyConverters.class})
    private Date trending__date;


    public Gif() {
    }

    public Gif(String type, String id, String url, GifImage images, String title, Date trending__date) {
        this.type = type;
        this.id = id;
        this.url = url;
        this.images = images;
        this.title = title;
        this.trending__date = trending__date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public GifImage getImages() {
        return images;
    }

    public void setImages(GifImage images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTrending__date() {
        return trending__date;
    }

    public void setTrending__date(Date trending__date) {
        this.trending__date = trending__date;
    }

}
