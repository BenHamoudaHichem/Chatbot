package com.example.hechbot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitAPI {
    @GET
    public Call<Response> getMessage(@Url String url);
}
