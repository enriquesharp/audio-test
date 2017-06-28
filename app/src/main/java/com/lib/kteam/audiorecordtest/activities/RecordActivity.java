package com.lib.kteam.audiorecordtest.activities;

import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import com.lib.kteam.audiorecordtest.AudioApp;
import com.lib.kteam.audiorecordtest.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Enrique on 6/26/2017.
 */

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    final static String AUDIO_PREFIX_NAME = "Audio recorded ";
    View startRecordBox;
    View stopRecordBox;
    AppCompatImageView startIcon;
    AppCompatImageView stopIcon;
    MediaRecorder mediaRecorder;
    File audioOutputFile = null;
    String audioName = "";
    Chronometer recordingTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        startRecordBox = findViewById(R.id.start_record_box);
        stopRecordBox = findViewById(R.id.stop_record_box);

        startIcon = (AppCompatImageView) findViewById(R.id.start_icon);
        stopIcon = (AppCompatImageView) findViewById(R.id.stop_icon);

        recordingTime = (Chronometer) findViewById(R.id.recording_time);

        updateVisualState(RECORD_STATE.STOPPED);

    }

    @Override
    protected void onResume() {
        super.onResume();

        setListeners();
    }

    private void setListeners() {
        startRecordBox.setOnClickListener(this);
        stopRecordBox.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == startRecordBox.getId()) {
            startRecord();
        } else if (viewId == stopRecordBox.getId()) {
            stopRecord();
            setAudioFileName();
        }
    }

    @Override
    public void onBackPressed() {
        stopRecord();
        super.onBackPressed();
    }

    void updateVisualState(RECORD_STATE recordState) {
        if (recordState.compareTo(RECORD_STATE.RECORDING) == 0) {
            startIcon.setEnabled(false);
            stopIcon.setEnabled(true);
            startRecordBox.setEnabled(false);
            stopRecordBox.setEnabled(true);
            recordingTime.invalidate();
            recordingTime.start();
        } else if (recordState.compareTo(RECORD_STATE.STOPPED) == 0) {
            startIcon.setEnabled(true);
            stopIcon.setEnabled(false);
            startRecordBox.setEnabled(true);
            stopRecordBox.setEnabled(false);
            recordingTime.stop();
        }
    }

    void startRecord() {
        try {
            initRecord();

            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(getApplicationContext(), "Recording...", Toast.LENGTH_SHORT).show();

            updateVisualState(RECORD_STATE.RECORDING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRecord() {
        audioName = generateAudioName();
        audioOutputFile = new File(AudioApp.getInstance().getAudioFilesBaseDir(), audioName + ".3gpp");

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(audioOutputFile.getAbsolutePath());
    }

    private String generateAudioName(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss", Locale.getDefault());
        return AUDIO_PREFIX_NAME.concat(dateFormat.format(new Date()));
    }

    void stopRecord() {
        try {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            Toast.makeText(getApplicationContext(), "Stopped...", Toast.LENGTH_SHORT).show();
            updateVisualState(RECORD_STATE.STOPPED);
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
                .setTitle(getResources().getString(R.string.set_file_name))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        audioOutputFile.renameTo(new File(AudioApp.getInstance().getAudioFilesBaseDir(), audioFileNameTv.getText() + ".3gpp"));
                        recordingTime.setBase(SystemClock.elapsedRealtime());
                    }
                });

        builder.create();
        builder.show();
    }

    private enum RECORD_STATE {RECORDING, STOPPED}
}
