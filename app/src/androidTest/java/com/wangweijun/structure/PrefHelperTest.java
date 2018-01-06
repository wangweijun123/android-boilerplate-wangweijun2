package com.wangweijun.structure;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.wangweijun.structure.data.local.pref.PreferencesHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PrefHelperTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        Log.i("wang", "log xxx daoSession:");
        assertEquals("com.wangweijun.structure", appContext.getPackageName());
    }

    @Test
    public void testPreferencesHelper() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        PreferencesHelper helper = new PreferencesHelper(InstrumentationRegistry.getTargetContext());
        helper.setScore(5);// 数据进入了手手机app
        float score = helper.getScore();
        Log.i("wang","score:"+score);
    }

}
