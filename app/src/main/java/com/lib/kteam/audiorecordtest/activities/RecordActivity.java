package com.lib.kteam.audiorecordtest.activities;

import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import com.lib.kteam.audiorecordtest.AudioApp;
import com.lib.kteam.audiorecordtest.R;

import java.io.File;
import java.util.UUID;

/**
 * Created by Enrique on 6/26/2017.
 */

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton startRecordBtn;
    AppCompatButton stopRecordBtn;

    MediaRecorder mediaRecorder;
    File audioOutputFile = null;
    String audioName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        startRecordBtn = (AppCompatButton) findViewById(R.id.start_btn);
        stopRecordBtn = (AppCompatButton) findViewById(R.id.stop_btn);

        startRecordBtn.setEnabled(true);
        stopRecordBtn.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setListeners();
    }

    private void initRecord() {
        audioName = UUID.randomUUID().toString();
        audioOutputFile = new File(AudioApp.getInstance().getAudioFilesBaseDir(), audioName + ".3gpp");

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(audioOutputFile.getAbsolutePath());
    }

    private void setListeners() {
        startRecordBtn.setOnClickListener(this);
        stopRecordBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == startRecordBtn.getId()) {
            startRecord();
        } else if (viewId == stopRecordBtn.getId()) {
            stopRecord();
        }
    }


    void startRecord() {
        try {
            initRecord();

            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(getApplicationContext(), "Recording...", Toast.LENGTH_SHORT).show();

            stopRecordBtn.setEnabled(true);
            startRecordBtn.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void stopRecord() {
        try {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            Toast.makeText(getApplicationContext(), "Stopped...", Toast.LENGTH_SHORT).show();
            stopRecordBtn.setEnabled(false);
            startRecordBtn.setEnabled(true);
            setAudioFileName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setAudioFileName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View rootView = getLayoutInflater().inflate(R.layout.dialog_audio_name_layout, null);
        final AppCompatEditText audioFileNameTv = ((AppCompatEditText) rootView.findViewById(R.id.audio_file_name_tv));
        audioFileNameTv.setText(audioOutputFile.getName().split("[.]")[0]);
        builder.setView(rootView)
                .setTitle("test")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        audioOutputFile.renameTo(new File(AudioApp.getInstance().getAudioFilesBaseDir(), audioFileNameTv.getText() + ".3gpp"));
                    }
                });

        builder.create();
        builder.show();
    }
}
