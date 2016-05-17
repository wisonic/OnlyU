package com.moje.onlyu.data.api;

import com.moje.onlyu.model.User;
import java.util.List;

import retrofit2.Callback;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2016/4/26.
 */
public interface ApiService {

    @GET("/users")
    public void getUsers(Callback<List<User>> callback);
}
