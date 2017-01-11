package com.example.android.popularmoviesstagetwo;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Peretz on 2017-01-11.
 */
public class MovieClient {
    public static Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.APIConstants.API_URL).addConverterFactory(GsonConverterFactory.create());
    private static OkHttpClient.Builder httpClient=new OkHttpClient.Builder();
    public static <S> S createService(Class<S> serviceClass){
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original=chain.request();
                HttpUrl url=original.url().newBuilder().addQueryParameter(Constants.APIConstants.API_KEY_QUERY_PARAMETER,Constants.APIConstants.API_KEY_PAREMETER).build();
                Request request=original.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        });
        Retrofit retrofit=builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
