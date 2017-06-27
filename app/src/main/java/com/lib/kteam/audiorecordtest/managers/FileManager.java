package com.lib.kteam.audiorecordtest.managers;

import android.webkit.MimeTypeMap;

import com.lib.kteam.audiorecordtest.AudioApp;
import com.lib.kteam.audiorecordtest.models.AudioModel;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Enrique on 6/26/2017.
 */

public class FileManager {

    public static File createFile(String file) {
        return null;
    }

    public static ArrayList<AudioModel> getAudioFiles() {
        String baseDir = AudioApp.getInstance().getAudioFilesBaseDir();
        ArrayList<AudioModel> availableFileList = new ArrayList<>();
        File rootFile = new File(baseDir);
        String[] fileList = rootFile.list();
        if (rootFile.exists() && fileList != null && fileList.length > 0) {
            for (String fName : fileList) {
                File f = new File(fName);
                String mimeType = getMimeType(f.getAbsolutePath());
                if (mimeType != null && mimeType.startsWith("audio")) {
                    AudioModel model = new AudioModel();
                    model.setFileName(f.getName());
                    availableFileList.add(model);
                }
            }
        }
        return availableFileList;
    }


    public static String getMimeType(String filePath) {
        String mimeType = null;
        String ext = MimeTypeMap.getFileExtensionFromUrl(filePath);

        mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);

        return mimeType;
    }
}
