package com.lib.kteam.audiorecordtest.models;

import java.util.Date;

/**
 * Created by Enrique on 6/26/2017.
 */

public class AudioModel {

    String fileName = "";
    Date createdDate = null;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
