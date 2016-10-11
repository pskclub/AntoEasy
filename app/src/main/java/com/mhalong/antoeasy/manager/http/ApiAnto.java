package com.mhalong.antoeasy.manager.http;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by passa on 10/10/2559.
 */

public interface ApiAnto {
    @GET("channel/get/{key}/{think}/{channel}")
    Call<ResponseBody> getAntoChannel(
            @Path("key") String key,
            @Path("think") String think,
            @Path("channel") String channel
    );

    @GET("channel/set/{key}/{think}/{channel}/{value}")
    Call<ResponseBody> setAntoChannel(
            @Path("key") String key,
            @Path("think") String think,
            @Path("channel") String channel,
            @Path("value") int value
    );
}
