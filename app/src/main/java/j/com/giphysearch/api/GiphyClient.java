package j.com.giphysearch.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GiphyClient {

    private static Retrofit mInstance = null;
    private static final String BASE_URL = "https://api.giphy.com";

    public static Retrofit getInstance(){
        if (mInstance == null){
            mInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mInstance;
    }



}
