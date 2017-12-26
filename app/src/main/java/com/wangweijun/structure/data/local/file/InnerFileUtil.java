package com.wangweijun.structure.data.local.file;

import android.content.Context;

import com.wangweijun.structure.data.model.Contributor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangweijun1 on 2017/12/26.
 */

public class InnerFileUtil {
    public static final String PREF_FILE_NAME = "homepage";

    File cacheDir;

    public InnerFileUtil(Context context) {
        cacheDir = new File(context.getExternalCacheDir(), PREF_FILE_NAME);
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
    }

    String key = "xxx";
    public void write(List<Contributor> contributors) {
        ObjectOutputStream oos = null;
        try {
            File file = new File(cacheDir, key);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(contributors);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
           if (oos != null) {
               try {
                   oos.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
        }
    }

    public List<Contributor> read() {
        String key = "xxx";
        ObjectInputStream oos = null;
        List<Contributor> contributors = new ArrayList<>();
        try {
            File cacheFile = new File(cacheDir, key);
            if (cacheFile.exists()) {
                oos = new ObjectInputStream(new FileInputStream(cacheFile));
                List<Contributor> list = (List<Contributor>)oos.readObject();
                if (list != null && list.size() > 0) {
                    contributors.addAll(list);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return contributors;
    }
}
