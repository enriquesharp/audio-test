package com.lib.kteam.audiorecordtest.adapters;

import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
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
    private AudioModel audioModelSelected = null;
    private AudioListener audioListener = null;
    private Runnable longPress = new Runnable() {
        public void run() {
            if (audioListener != null)
                audioListener.onAudioLongPressed(audioModelSelected);
        }
    };

    public void setData(ArrayList<AudioModel> audioModels, AudioListener listener) {
        this.audioListener = listener;
        audioModelSelected = null;
        if (audioModels != null) {
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

        holder.lineDivider.setVisibility(holder.getAdapterPosition() == audioModelArrayList.size() - 1 ? View.INVISIBLE : View.VISIBLE);

        setItemViewTouchInteraction(holder);
    }

    @Override
    public int getItemCount() {
        return audioModelArrayList.size();
    }

    private void setItemViewTouchInteraction(final AudioAdapterHolder holder) {
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                audioModelSelected = audioModelArrayList.get(holder.getAdapterPosition());

                if (audioListener != null)
                    audioListener.onAudioLongPressed(audioModelSelected);

//                holder.cardView.setCardBackgroundColor(AudioApp.getInstance().getResources().getColor(R.color.lightest_gray_color));
//                handler.postDelayed(longPress, 1000);
                return true;
            }
        });
        holder.cardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                switch (action) {
                    case (MotionEvent.ACTION_UP): {
                        audioModelSelected = audioModelArrayList.get(holder.getAdapterPosition());
//                        holder.cardView.setCardBackgroundColor(AudioApp.getInstance().getResources().getColor(android.R.color.white));
                        if (audioListener != null)
                            audioListener.onAudioStopPressed(audioModelSelected);
                        handler.removeCallbacks(longPress);
                    }
                    break;
                }

                return false;
            }
        });
    }

    class AudioAdapterHolder extends RecyclerView.ViewHolder {

        View itemBox;
        AppCompatTextView audioDir;
        View lineDivider;
        CardView cardView;

        AudioAdapterHolder(View itemView) {
            super(itemView);
            itemBox = itemView.findViewById(R.id.item_box);
            audioDir = (AppCompatTextView) itemView.findViewById(R.id.audio_dir_tv);
            lineDivider = itemView.findViewById(R.id.line_divider);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}
