package app.horecafy.com.Retrofit;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by aipxperts on 14/8/18.
 */

public interface APIService {
    @Multipart
    @PUT("offer/{offerid}")
    Call<ResponseBody> UploadArray(@Path("offerid") String offerid,
                                   @Part List<MultipartBody.Part> image,
                                   @Part MultipartBody.Part video);

    @Multipart
    @PUT("businessvisit/upload/{group_Id}")
    Call<ResponseBody> proposalUploadArray(@Path("group_Id") String group_Id,
                                   @Part List<MultipartBody.Part> image,
                                   @Part MultipartBody.Part video);

    @POST("wholesaler-list/category")
    @FormUrlEncoded
    Call<ResponseBody> AddCategory(@Field("wholesalerId") String id,
                              @Field("categoryIds") String category);

    @GET("province/")
    Call<ResponseBody> getProvincia();
}

