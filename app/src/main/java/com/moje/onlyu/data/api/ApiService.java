package com.moje.onlyu.data.api;

import com.moje.onlyu.model.User;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2016/4/26.
 */
public interface ApiService {
    @GET("/user")
    Observable<Response<User>> getUser(@Path("owner") String owner , @Path("repo") String repo);
}
