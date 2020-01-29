package com.nasande.nasande;

import com.nasande.nasande.model.Node;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @POST("user/login?_format=json")
    @Headers({"Content-type: application/json"})
    Call<ResponseBody> loginRequest(@Body LoginData body);

    @Multipart
    @POST("upload")
    Call<ResponseBody> upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );


    @POST ("file/upload/node/article/field_image?_format=json")
    Call<ResponseBody> postFile (@Header("Authorization") String authorization,@Header("X-CSRF-Token") String x_csrf_token,@Header("Content-Disposition") String content_disposition, @Body RequestBody field_image);

    @POST ("file/upload/node/audio/field_fichier_audio?_format=json")
    Call<ResponseBody> postAudio (@Header("Authorization") String authorization,@Header("X-CSRF-Token") String x_csrf_token,@Header("Content-Disposition") String content_disposition, @Body RequestBody field_image);

    @POST("/node?_format=hal_json")
    Call<ResponseBody> addNode(@Header("Authorization") String user_auth, @Header("X-CSRF-Token") String x_csrf_token, @Body Node node);


}