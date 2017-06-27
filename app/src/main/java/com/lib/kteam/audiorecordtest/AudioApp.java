package com.lib.kteam.audiorecordtest;

import android.app.Application;
import android.os.Environment;

import java.io.File;

/**
 * Created by Enrique on 6/26/2017.
 */

public class AudioApp extends Application {

    private static AudioApp instance;

    public static AudioApp getInstance() {
        if (instance == null)
            instance = new AudioApp();
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public String getAudioFilesBaseDir(){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/AudioContents/");
        if(!file.exists())
            file.mkdir();
        return file.getAbsolutePath();
    }
}
