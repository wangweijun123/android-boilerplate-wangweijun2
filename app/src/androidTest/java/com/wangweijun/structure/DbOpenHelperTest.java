package com.wangweijun.structure;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.wangweijun.structure.data.local.db.DaoMaster;
import com.wangweijun.structure.data.local.db.DaoSession;
import com.wangweijun.structure.data.local.db.DbOpenHelper;
import com.wangweijun.structure.data.local.db.MigrationHelper;
import com.wangweijun.structure.data.model.Contributor;

import org.greenrobot.greendao.database.Database;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DbOpenHelperTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        Log.i("wang", "log xxx daoSession:");
        assertEquals("com.wangweijun.structure", appContext.getPackageName());
    }

    @Test
    public void testPreferencesHelper() throws Exception {

        DaoSession daoSession = provideDaoSession();
        Contributor account = new Contributor();
        account.setAvatar_url("http://www.baidu.com");
        account.setContributions(233);
        account.setLogin("刘德华");

        daoSession.getContributorDao().insert(account);
        List<Contributor> list = daoSession.getContributorDao().queryBuilder().list();
        for (Contributor contributor : list) {
            Log.i("wang", "contributor:"+contributor);
        }
    }

    public DaoSession provideDaoSession() {
        /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
        boolean ENCRYPTED = false;
        MigrationHelper.DEBUG = true;
        DbOpenHelper helper2 = new DbOpenHelper(InstrumentationRegistry.getTargetContext(), ENCRYPTED ? "notes-db-encrypted" : "notes-db",
                null);
        Database db = ENCRYPTED ? helper2.getEncryptedWritableDb("super-secret") : helper2.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }

}
