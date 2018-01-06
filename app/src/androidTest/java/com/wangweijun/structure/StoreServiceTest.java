package com.wangweijun.structure;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.wangweijun.structure.data.model.AppDetailsModel;
import com.wangweijun.structure.data.model.IResponse;
import com.wangweijun.structure.data.remote.StoreService;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class StoreServiceTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        Log.i("wang", "log xxx daoSession:");
        assertEquals("com.wangweijun.structure", appContext.getPackageName());
    }

    @Test
    public void testStoreService() throws Exception {
        provideStoreService().getAppDetail("com.achievo.vipshop")
                .subscribeOn(Schedulers.io())
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
                        Log.i("wang", "onError score:");
                    }

                    @Override
                    public void onComplete() {
                        Log.i("wang", "onComplete score:");
                    }
                });;

    }


    public StoreService provideStoreService() {
        return StoreService.Creator.newStoreService();
    }

}
