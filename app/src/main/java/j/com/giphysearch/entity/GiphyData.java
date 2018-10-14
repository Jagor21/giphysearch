package j.com.giphysearch.entity;

import android.arch.persistence.room.Entity;

import java.util.List;

public class GiphyData {

    private List<Gif> data;

    public GiphyData() {
    }

    public GiphyData(List<Gif> data) {
        this.data = data;
    }

    public List<Gif> getData() {
        return data;
    }

    public void setData(List<Gif> data) {
        this.data = data;
    }
}
