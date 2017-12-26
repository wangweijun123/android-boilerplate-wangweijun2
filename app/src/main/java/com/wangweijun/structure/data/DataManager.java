package com.wangweijun.structure.data;

import android.util.Log;

import com.wangweijun.structure.data.local.db.Account;
import com.wangweijun.structure.data.local.db.DaoSession;
import com.wangweijun.structure.data.local.pref.PreferencesHelper;
import com.wangweijun.structure.data.model.AppDetailsModel;
import com.wangweijun.structure.data.model.Contributor;
import com.wangweijun.structure.data.model.HomePageModel;
import com.wangweijun.structure.data.model.IResponse;
import com.wangweijun.structure.data.model.RankListModel;
import com.wangweijun.structure.data.remote.GithubService;
import com.wangweijun.structure.data.remote.StoreService;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by wangweijun1 on 2017/12/6.
 */

public class DataManager {

    private PreferencesHelper mPreferencesHelper;

    private StoreService mStoreService;

    private GithubService mGithubService;

    private DaoSession mDaoSession;

    public DataManager(StoreService storeService,GithubService githubService, PreferencesHelper preferencesHelper, DaoSession daoSession) {
        mStoreService = storeService;
        mGithubService = githubService;
        mPreferencesHelper = preferencesHelper;
        mDaoSession = daoSession;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<IResponse<HomePageModel>> getHomePageRequest(Map<String, String> params,Map<String, String> headers) {
        return mStoreService.getHomePageRequest(params, headers);
    }

    public Observable<IResponse<RankListModel>> getRankApps(String pagefrom, String pagesize,String code) {
        return mStoreService.getRankApps(pagefrom, pagesize, code);
    }

    public Observable<IResponse<AppDetailsModel>> getAppDetail(String packagename) {
        return mStoreService.getAppDetail(packagename);
    }
    static int count = 0;
    public Observable<List<Contributor>> syncContributorsSaveDB() {
        return mGithubService.contributors("square", "retrofit")
                .concatMap(new Function<List<Contributor>, ObservableSource<List<Contributor>>>() {
                    @Override
                    public ObservableSource<List<Contributor>> apply(@NonNull final List<Contributor> contributors)
                            throws Exception {
                        Log.i("wang", "网络请求concatMap apply tid:"+Thread.currentThread().getId());
                       return Observable.create(new ObservableOnSubscribe<List<Contributor>>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<List<Contributor>> emitter) throws Exception {
                                Log.i("wang", "apply tid:"+Thread.currentThread().getId());
                                mDaoSession.getContributorDao().deleteAll();
                                count++;
                                for (Contributor contributor : contributors) {
                                    contributor.login = contributor.login+count;
                                }
                                mDaoSession.getContributorDao().insertInTx(contributors);
                                emitter.onNext(contributors);
                                emitter.onComplete();
                            }
                        });
                    }
                });
    }

    public Observable<List<Contributor>> loadContributorsFromDB() {
        return Observable.create(new ObservableOnSubscribe<List<Contributor>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Contributor>> emitter) throws Exception {
                Log.i("wang", "query DB subscribe  tid:"+Thread.currentThread().getId());
                List<Contributor> cacheList = mDaoSession.getContributorDao().queryBuilder().list();
                emitter.onNext(cacheList);
                emitter.onComplete();
            }
        });
    }


    public Observable<List<Contributor>> loadContributors() {
        return Observable.create(new ObservableOnSubscribe<List<Contributor>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Contributor>> emitter) throws Exception {
                Log.i("wang", "query DB subscribe  tid:"+Thread.currentThread().getId());
                List<Contributor> cacheList = mDaoSession.getContributorDao().queryBuilder().list();
                emitter.onNext(cacheList);
            }
        }).concatMap(new Function<List<Contributor>, ObservableSource<? extends List<Contributor>>>() {
            @Override
            public ObservableSource<? extends List<Contributor>> apply(@NonNull List<Contributor> contributors) throws Exception {
                Log.i("wang", " concatMap apply tid:"+Thread.currentThread().getId());
                return syncContributorsSaveDB();
            }
        });
    }

    public Observable<Contributor> syncContributors2() {
        return mGithubService.contributors("square", "retrofit")
                .concatMap(new Function<List<Contributor>, ObservableSource<Contributor>>() {
                    @Override
                    public ObservableSource<Contributor> apply(@NonNull final List<Contributor> contributors)
                            throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Contributor>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<Contributor> emitter) throws Exception {
                                Log.i("wang", "apply tid:"+Thread.currentThread().getId());
                                for (Contributor contributor : contributors) {
                                    long result =  mDaoSession.getContributorDao().insert(contributor);
                                    if (result >= 0) {
                                        Log.i("wang", " emitter 发射 contributor:"+contributor);
                                        emitter.onNext(contributor);
                                    }
                                }
                                emitter.onComplete();
                            }
                        });
                    }
                });
    }

    public Observable<List<Contributor>> syncContributorsSaveToPref() {
        return mGithubService.contributors("square", "retrofit")
                .concatMap(new Function<List<Contributor>, ObservableSource<List<Contributor>>>() {
                    @Override
                    public ObservableSource<List<Contributor>> apply(@NonNull final List<Contributor> contributors)
                            throws Exception {
                        Log.i("wang", "网络请求concatMap apply tid:"+Thread.currentThread().getId());
                        return Observable.create(new ObservableOnSubscribe<List<Contributor>>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<List<Contributor>> emitter) throws Exception {
                                Log.i("wang", "apply tid:"+Thread.currentThread().getId());
                                count++;
                                for (Contributor contributor : contributors) {
                                    contributor.login = contributor.login+count;
                                }
                                emitter.onNext(contributors);
                                emitter.onComplete();
                            }
                        });
                    }
                });
    }

    public Observable<Account> insertAccount(final Account account) {
        return Observable.create(new ObservableOnSubscribe<Account>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Account> emitter) throws Exception {
                mDaoSession.getAccountDao().insert(account);
                emitter.onComplete();
            }
        });
    }

    public Observable insertAccounts(final List<Account> accounts) {
        return Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(@NonNull ObservableEmitter emitter) throws Exception {
                Log.i("wang", "subscribe tid:"+Thread.currentThread().getId());
                // 开启事务批量插入
                mDaoSession.getAccountDao().insertInTx(accounts);
                emitter.onComplete();
            }
        });
    }

    public Observable<List<Account>> queryAccounts() {
        return Observable.create(new ObservableOnSubscribe<List<Account>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Account>> emitter) throws Exception {
                List<Account> accounts = mDaoSession.getAccountDao().queryBuilder().build().list();
                emitter.onNext(accounts);
                emitter.onComplete();
            }
        });
    }

}
