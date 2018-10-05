package app.horecafy.com.Retrofit;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by aipxperts on 14/8/18.
 */

public class ApiClient {
  //  public static final String BASE_URL = "http://demo.aipxperts.com:4201/api/v1/offer/";
    public static final String BASE_URL = "http://demo.aipxperts.com:4201/api/v1/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Activity context) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.SECONDS)
                .addInterceptor(new ConnectivityInterceptor(context))
                .readTimeout(30000, TimeUnit.SECONDS).build();

        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …
// add logging as last interceptor
            httpClient.addInterceptor(logging); // <-- this is the important line!
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit;
    }

    @NotNull
    public static String getGroupID(@Nullable String json) {
        String gid = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            gid = jsonObject1.getString("groupId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gid;
    }

}
