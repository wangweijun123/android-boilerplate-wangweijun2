package com.wangweijun.structure;

import android.util.Log;

import com.wangweijun.structure.data.model.AppDetailsModel;
import com.wangweijun.structure.data.model.IResponse;
import com.wangweijun.structure.data.remote.StoreService;
import com.wangweijun.structure.util.StringUtil;

import org.junit.Test;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void testStringSplite() throws Exception {
        String arr[] = StringUtil.splite("dddddd-ddddd-eeee-ttt");
        for (int i=0; i<arr.length; i++) {
//            Log.i("wang", "xxx");// java test 不能用android.log
            System.out.println(arr[i]);
        }

    }

    @Test
    public void testStoreService() throws Exception {
        provideStoreService().getAppDetail("com.achievo.vipshop")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<IResponse<AppDetailsModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull IResponse<AppDetailsModel> appDetailsModelIResponse) {
                        AppDetailsModel appDetailsModel = appDetailsModelIResponse.getEntity();
                        Log.i("wang", "onNext score:" + appDetailsModel.score + ", tid:" + Thread.currentThread().getId());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });;

    }


    public StoreService provideStoreService() {
        return StoreService.Creator.newStoreService();
    }


}