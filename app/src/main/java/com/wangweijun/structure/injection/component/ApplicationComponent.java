package com.wangweijun.structure.injection.component;

import android.content.Context;

import com.wangweijun.structure.data.DataManager;
import com.wangweijun.structure.data.local.db.DaoSession;
import com.wangweijun.structure.data.local.file.InnerFileUtil;
import com.wangweijun.structure.data.local.pref.PreferencesHelper;
import com.wangweijun.structure.data.remote.GithubService;
import com.wangweijun.structure.data.remote.StoreService;
import com.wangweijun.structure.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by wangweijun1 on 2017/12/6.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    StoreService provideStoreService();// 告诉依赖组件自己能提供什么样的实例
    GithubService provideGithubService();
    DataManager provideDataManager();// 告诉依赖组件自己能提供什么样的实例
    Context provideContext();// 告诉依赖组件自己能提供什么样的实例
    PreferencesHelper providePreferencesHelper(); //告诉依赖组件自己能提供什么样的实例
    DaoSession provideDaoSession();//告诉依赖组件自己能提供什么样的实例
    InnerFileUtil provideInnerFileUtil();
}
