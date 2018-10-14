package j.com.giphysearch.api;

import j.com.giphysearch.entity.GiphyData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GiphyApi {


    @GET("/v1/gifs/search?")
    Call<GiphyData> searchForGifs(@Query("q") String searchKey, @Query("api_key") String apiKey);

    @GET("/v1/gifs/trending?")
    Call<GiphyData> getTrendingGifs(@Query("api_key") String apiKey);

}
