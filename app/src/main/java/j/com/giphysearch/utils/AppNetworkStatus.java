package j.com.giphysearch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppNetworkStatus {

    static Context mContext;

    private static AppNetworkStatus mInstance = new AppNetworkStatus();

    private ConnectivityManager manager;

    private boolean isOnline = false;

    public static AppNetworkStatus getInstance(Context context){
        mContext = context;
        return mInstance;
    }

    public boolean isOnline() {
        try {
            manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();

            isOnline = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return isOnline;
        }catch (Exception e){
            System.out.println("My Connection check exception: " + e.getMessage());
        }
        return isOnline;
    }
}
