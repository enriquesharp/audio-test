package com.lib.kteam.audiorecordtest.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
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

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AudioListener {

    final static int PERMISSIONS_CODE = 1111;
    FloatingActionButton fab;

    RecyclerView audioRv;
    AudioAdapter audioAdapter;
    AppCompatTextView emptyListTv;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        audioRv = (RecyclerView) findViewById(R.id.audio_rv);

        emptyListTv = (AppCompatTextView) findViewById(R.id.empty_list_tv);

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
            ArrayList<String> permission = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
                permission.add(Manifest.permission.RECORD_AUDIO);

            if (permission.size() > 0) {
                String[] per = new String[permission.size()];

                for(int i = 0; i < permission.size(); i++){
                    per[i] = permission.get(i);
                }

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

        if (audioFiles != null && audioFiles.size() > 0) {
            audioAdapter.setData(audioFiles, this);
            audioRv.setVisibility(View.VISIBLE);
            emptyListTv.setVisibility(View.GONE);
        } else {
            audioRv.setVisibility(View.GONE);
            emptyListTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_help) {
            launchHelpDialog();
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
            stopAudio(audioModel);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
            mediaPlayer.setDataSource(new File(AudioApp.getInstance().getAudioFilesBaseDir(), audioModel.getFileName()).getAbsolutePath());
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

    void launchHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View rootView = getLayoutInflater().inflate(R.layout.dialog_help_layout, null);
        builder.setView(rootView)
                .setTitle(getResources().getString(R.string.help_title))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        builder.create();
        builder.show();
    }
}
