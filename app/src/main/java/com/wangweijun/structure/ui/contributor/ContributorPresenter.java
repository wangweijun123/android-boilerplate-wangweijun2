package com.wangweijun.structure.ui.contributor;

import android.util.Log;

import com.wangweijun.structure.data.DataManager;
import com.wangweijun.structure.data.model.Contributor;
import com.wangweijun.structure.ui.base.BasePresenter;
import com.wangweijun.structure.ui.blacklist.BlacklistMvpView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wangweijun1 on 2017/12/7.
 */

public class ContributorPresenter extends BasePresenter<BlacklistMvpView> {

    private final DataManager mDataManager;

    @Inject
    public ContributorPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void syncContributors() {
        Log.i("wang", " #######syncContributorsSaveDB######## ");
        mDataManager.syncContributorsSaveDB()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<Contributor>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i("wang", "onSubscribe tid:" + Thread.currentThread().getId());
            }

            @Override
            public void onNext(@NonNull List<Contributor> account) {
                Log.i("wang", "onNext tid:" + Thread.currentThread().getId()+", size:"+account.size());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i("wang", "onError tid:" + Thread.currentThread().getId());
            }

            @Override
            public void onComplete() {
                Log.i("wang", "onComplete tid:" + Thread.currentThread().getId());
            }
        });
    }

    public void loadContributorsFromDB() {
        Log.i("wang", " #######loadContributorsFromDB######## ");
        mDataManager.loadContributorsFromDB()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Contributor>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("wang", "onSubscribe tid:" + Thread.currentThread().getId());
                    }

                    @Override
                    public void onNext(@NonNull List<Contributor> list) {
                        Log.i("wang", "onNext tid:" + Thread.currentThread().getId()+", size:"+list.size());
                        for (Contributor contributor : list) {
                            Log.i("wang",contributor.toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i("wang", "onError tid:" + Thread.currentThread().getId());
                        syncContributors();
                    }

                    @Override
                    public void onComplete() {
                        Log.i("wang", "onComplete tid:" + Thread.currentThread().getId());
                        syncContributors();
                    }
                });
    }

    public void loadContributorsFromDBOnly() {
        Log.i("wang", " #######loadContributorsFromDBOnly######## ");
        mDataManager.loadContributorsFromDB()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Contributor>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("wang", "onSubscribe tid:" + Thread.currentThread().getId());
                    }

                    @Override
                    public void onNext(@NonNull List<Contributor> list) {
                        Log.i("wang", "onNext tid:" + Thread.currentThread().getId()+", size:"+list.size());
                        for (Contributor contributor : list) {
                            Log.i("wang",contributor.toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i("wang", "onError tid:" + Thread.currentThread().getId());
                        syncContributors();
                    }

                    @Override
                    public void onComplete() {
                        Log.i("wang", "onComplete tid:" + Thread.currentThread().getId());
                    }
                });
    }

    /**
     * 1 读取数据库缓存(io)，返回显示缓存(ui)
     * 2 网络加载,更新数据库(io)
     * 3 更新UI
     */
    public void load() {

        mDataManager.loadContributorsFromDB()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<List<Contributor>>() {
                    @Override
                    public void accept(@NonNull List<Contributor> contributors) throws Exception {
                        Log.i("wang", "缓存返回 accept tid:" + Thread.currentThread().getId());
                        for (Contributor contributor : contributors) {
                            Log.i("wang", contributor.toString());
                        }
                    }
                }).observeOn(Schedulers.io())
                .flatMap(new Function<List<Contributor>, ObservableSource<List<Contributor>>>() {
                    @Override
                    public ObservableSource<List<Contributor>> apply(@NonNull List<Contributor> contributors) throws Exception {
                        return mDataManager.syncContributorsSaveDB();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Contributor>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i("wang", "onSubscribe tid:" + Thread.currentThread().getId());
                    }

                    @Override
                    public void onNext(@NonNull List<Contributor> list) {
                        Log.i("wang", "网络返回onNext tid:" + Thread.currentThread().getId() + ", size:" + list.size());
                        for (Contributor contributor : list) {
                            Log.i("wang", contributor.toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i("wang", "onError tid:" + Thread.currentThread().getId());
                        syncContributors();
                    }

                    @Override
                    public void onComplete() {
                        Log.i("wang", "onComplete tid:" + Thread.currentThread().getId());
                    }
                });
    }




}
