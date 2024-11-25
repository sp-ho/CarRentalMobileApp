package com.example.carrentalproject;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class CarService extends Service {
    MediaPlayer myPlayer;
    public CarService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service created", Toast.LENGTH_LONG).show();
        // find the media file
        myPlayer = MediaPlayer.create(this, R.raw.take_me_home_country_road);
        // no looping
        myPlayer.setLooping(false);
    }

    // start the media
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();
        myPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    // stop the media
    @Override
    public void onDestroy(){
        super.onDestroy();
        Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
        myPlayer.stop();
    }
}