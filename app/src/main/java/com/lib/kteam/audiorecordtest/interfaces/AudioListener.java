package com.lib.kteam.audiorecordtest.interfaces;

import com.lib.kteam.audiorecordtest.models.AudioModel;

/**
 * Created by Enrique on 6/26/2017.
 */

public interface AudioListener {

    void onAudioLongPressed(AudioModel audioModel);

    void onAudioStopPressed(AudioModel audioModel);
}
