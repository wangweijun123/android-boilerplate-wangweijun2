package com.wangweijun.structure.data.remote;

import com.wangweijun.structure.data.model.Contributor;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubService {

    String ENDPOINT = "https://api.github.com";

    // https://api.github.com/repos/square/retrofit/contributors
    @GET("/repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> contributors(
            @Path("owner") String owner,
            @Path("repo") String repo);


    /******** Helper class that sets up a new services *******/
    class Creator {

        public static GithubService newGithubService() {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GithubService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(OkHttpUtils.getInstance().getOkHttpClient())
                    .build();
            return retrofit.create(GithubService.class);
        }
    }
}
