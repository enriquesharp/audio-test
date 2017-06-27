package com.lib.kteam.audiorecordtest.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lib.kteam.audiorecordtest.AudioApp;
import com.lib.kteam.audiorecordtest.R;
import com.lib.kteam.audiorecordtest.adapters.AudioAdapter;
import com.lib.kteam.audiorecordtest.interfaces.AudioListener;
import com.lib.kteam.audiorecordtest.managers.FileManager;
import com.lib.kteam.audiorecordtest.models.AudioModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AudioListener {

    final static int PERMISSIONS_CODE = 1111;
    FloatingActionButton fab;

    RecyclerView audioRv;
    AudioAdapter audioAdapter;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        audioRv = (RecyclerView) findViewById(R.id.audio_rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        audioRv.setLayoutManager(llm);

        audioAdapter = new AudioAdapter();
        audioRv.setAdapter(audioAdapter);

        checkOrRequestPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();

        setListeners();

        setAudioList();

    }

    void checkOrRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] per = new String[]{};
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                per[0] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
                per[1] = Manifest.permission.RECORD_AUDIO;

            if (per.length > 0) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.RECORD_AUDIO)) {

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, per, PERMISSIONS_CODE);
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_CODE: {
                if (grantResults.length > 0 && grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
            }
        }
    }

    private void setListeners() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                launchRecordActivity();
            }
        });
    }

    private void setAudioList() {
        ArrayList<AudioModel> audioFiles = FileManager.getAudioFiles();
        audioAdapter.setData(audioFiles, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAudioLongPressed(AudioModel audioModel) {
        if (audioModel != null)
            playAudio(audioModel);
    }

    @Override
    public void onAudioStopPressed(AudioModel audioModel) {
        if (audioModel != null)
            stopAudio(audioModel);
    }

    void launchRecordActivity() {
        Intent i = new Intent(MainActivity.this, RecordActivity.class);
        startActivity(i);
    }


    void playAudio(AudioModel audioModel) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(AudioApp.getInstance().getAudioFilesBaseDir() + audioModel.getFileName());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void stopAudio(AudioModel audioModel) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
