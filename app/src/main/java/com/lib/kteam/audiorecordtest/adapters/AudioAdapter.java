package com.lib.kteam.audiorecordtest.adapters;

import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.lib.kteam.audiorecordtest.AudioApp;
import com.lib.kteam.audiorecordtest.R;
import com.lib.kteam.audiorecordtest.interfaces.AudioListener;
import com.lib.kteam.audiorecordtest.models.AudioModel;

import java.util.ArrayList;

/**
 * Created by Enrique on 6/26/2017.
 */

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioAdapterHolder> {

    private final Handler handler = new Handler();
    private ArrayList<AudioModel> audioModelArrayList = new ArrayList<>();
    private AudioModel longPressedModel = null;
    private AudioListener audioListener = null;
    private Runnable longPress = new Runnable() {
        public void run() {
            if (audioListener != null)
                audioListener.onAudioLongPressed(longPressedModel);
        }
    };

    public void setData(ArrayList<AudioModel> audioModels, AudioListener listener) {
        this.audioListener = listener;
        longPressedModel = null;
        if (audioModels != null){
            this.audioModelArrayList = audioModels;
            notifyDataSetChanged();
        }
    }

    @Override
    public AudioAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(AudioApp.getInstance()).inflate(R.layout.audio_item_layout, parent, false);
        return new AudioAdapterHolder(v);
    }

    @Override
    public void onBindViewHolder(AudioAdapterHolder holder, int position) {
        final AudioModel model = audioModelArrayList.get(holder.getAdapterPosition());

        holder.audioDir.setText(model.getFileName().split("[.]")[0]);

        setItemViewTouchInteraction(holder);
    }

    @Override
    public int getItemCount() {
        return audioModelArrayList.size();
    }

    private void setItemViewTouchInteraction(final AudioAdapterHolder holder) {
        holder.itemBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                switch (action) {
                    case (MotionEvent.ACTION_DOWN): {
                        longPressedModel = audioModelArrayList.get(holder.getAdapterPosition());
                        handler.postDelayed(longPress, 1000);
                    }
                    break;
                    case (MotionEvent.ACTION_UP): {
                        longPressedModel = null;
                        handler.removeCallbacks(longPress);
                    }
                    break;
                    case (MotionEvent.ACTION_MOVE): {

                    }
                    break;
                }

                return true;
            }
        });
    }

    class AudioAdapterHolder extends RecyclerView.ViewHolder {

        View itemBox;
        AppCompatTextView audioDir;

        AudioAdapterHolder(View itemView) {
            super(itemView);
            itemBox = itemView.findViewById(R.id.item_box);
            audioDir = (AppCompatTextView) itemView.findViewById(R.id.audio_dir_tv);
        }
    }
}
