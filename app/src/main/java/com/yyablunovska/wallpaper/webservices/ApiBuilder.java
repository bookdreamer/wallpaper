package com.yyablunovska.wallpaper.webservices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yyablunovska.wallpaper.utils.Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author yuliia.yablunovska on 3/11/18.
 */

public class ApiBuilder {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static Retrofit sRetrofit;
    private static Gson sGson = new GsonBuilder().create();
    private static HttpLoggingInterceptor sLoggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
            .addInterceptor(sLoggingInterceptor)
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    final Request request = chain.request().newBuilder()
                            .addHeader(AUTHORIZATION_HEADER, "Client-ID " + Utils.APPLICATION_ID)
                            .build();
                    return chain.proceed(request);
                }
            });
    private static OkHttpClient sOkHttpClient = okHttpClientBuilder.build();

    public static <T> T getApi(Class<T> apiClass) {
        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .client(sOkHttpClient)
                    .baseUrl(Utils.BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create(sGson))
                    .build();
        }
        return sRetrofit.create(apiClass);
    }

}
