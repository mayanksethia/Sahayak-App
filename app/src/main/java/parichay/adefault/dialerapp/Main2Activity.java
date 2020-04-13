package parichay.adefault.dialerapp;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;


public class Main2Activity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    TextToSpeech t1;
    private FirebaseAnalytics mFirebaseAnalytics;
    String n1,n2,n3,n4,n5;
    String g1,g2,g3,g4,g5;
    int c1=0,c2=0,c3=0,c4=0,c5=0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;
    Song song;
    Button b1;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;


    WindowManager windowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR)
                {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        song = new Song();
        ref = FirebaseDatabase.getInstance().getReference().child("Song");

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;


        b1 = (Button)findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                }
                else if(!(mediaPlayer.isPlaying()))
                {
                    mediaPlayer.start();
                }
            }
        });

        Button backButton = (Button)this.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.speak("Back Button Pressed",TextToSpeech.QUEUE_FLUSH,null);

                finish();
            }
        });

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);




    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 12) {
                Toast.makeText(getApplicationContext(), "Shake event detected", Toast.LENGTH_SHORT).show();
                t1.speak("Shake Event Detected",TextToSpeech.QUEUE_FLUSH,null);

                Intent i = new Intent(Main2Activity.this,Main3Activity.class);
                startActivity(i);
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    int i = 1;
    String play_id;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {

            Toast.makeText(this, "Playing next song", Toast.LENGTH_LONG).show();
            i++;
            play_id  = Song_Identifier(i);
            play_song(play_id);
            return true;

        }
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            Toast.makeText(this, "Playing previous song", Toast.LENGTH_LONG).show();
            i--;
            play_id = Song_Identifier(i);
            play_song(play_id);
            return true;
        }
        else if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SLEEP || keyCode == KeyEvent.KEYCODE_POWER)
        {
            mediaPlayer.stop();

            Log.i("","Back button pressed now");
            Toast.makeText(this, "Stopping music player", Toast.LENGTH_SHORT).show();
            return super.onKeyDown(keyCode,event);
        }
        else {
            return super.onKeyDown(keyCode, event);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop(); // or mp.pause();
            mediaPlayer.release();
        }
    }



    public String song_id;
    public String Song_Identifier(int ind)
    {
        int ind1 = ind%5;
        if(ind1 == 0)
        {

            song_id = "https://firebasestorage.googleapis.com/v0/b/team-sahayak.appspot.com/o/BachGavotteShort.mp3?alt=media&token=e725759d-d868-4476-9acc-c0baba80ad5c";
            Toast.makeText(this, "Playing BachGavotteShort.mp3", Toast.LENGTH_SHORT).show();
            n1 = "BachGavotteShort";
            g1 = "Jazz";
            t1.speak("Playing BachGavotteShort",TextToSpeech.QUEUE_FLUSH,null);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(ind1));
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, n1);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, g1);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            String v1 = String.valueOf(c1++);

            song.setSongname(n1);
            song.setGenre(g1);
            ref.push().setValue(song);

        }
        else if(ind1==1)
        {
            song_id = "https://firebasestorage.googleapis.com/v0/b/team-sahayak.appspot.com/o/bensound-happyrock.mp3?alt=media&token=b9ba1557-6940-4c4f-9ef3-90468a5fbc00";
            n2 = "Happy Rock";
            g2 = "Rock";
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(ind1));
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, n2);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, g2);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            Toast.makeText(this, "Playing HappyRock.mp3", Toast.LENGTH_SHORT).show();
            t1.speak("Playing Happy Rock",TextToSpeech.QUEUE_FLUSH,null);
            String v2 = String.valueOf(c2++);
            song.setSongname(n2);
            song.setGenre(g2);
            ref.push().setValue(song);
        }
        else if(ind1==2)
        {
            song_id = "https://firebasestorage.googleapis.com/v0/b/team-sahayak.appspot.com/o/bensound-summer.mp3?alt=media&token=d91c680b-8120-4b14-93d9-50e0d5e9fcaa";
            n3 = "Summer";
            g3 = "Popular Music";

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(ind1));
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, n3);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, g3);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            Toast.makeText(this, "Playing Summer.mp3", Toast.LENGTH_SHORT).show();
            t1.speak("Playing Summer",TextToSpeech.QUEUE_FLUSH,null);

            song.setSongname(n3);
            song.setGenre(g3);
            ref.push().setValue(song);
        }
        else if(ind1==3) {
            song_id = "https://firebasestorage.googleapis.com/v0/b/team-sahayak.appspot.com/o/bensound-ukulele.mp3?alt=media&token=3b3b0749-68d7-41de-90cc-9acc8450e38c";
            n4 = "Ukelele";
            g4 = "Funk Music";

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(ind1));
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, n4);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, g4);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            Toast.makeText(this, "Playing Ukulele.mp3", Toast.LENGTH_SHORT).show();
            t1.speak("Playing Ukulele",TextToSpeech.QUEUE_FLUSH,null);
            song.setSongname(n4);
            song.setGenre(g4);
            ref.push().setValue(song);
        }
        else if(ind1==4)
        {
            song_id = "https://firebasestorage.googleapis.com/v0/b/team-sahayak.appspot.com/o/file_example_MP3_700KB.mp3?alt=media&token=d5a60392-232a-429d-9885-6a039b88f714";

            n5 = "Masakali";
            g5 = "Bollywood";

            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(ind1));
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, n5);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, g5);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

            Toast.makeText(this, "Playing Masakali.mp3", Toast.LENGTH_SHORT).show();
            t1.speak("Playing Masakali",TextToSpeech.QUEUE_FLUSH,null);

            song.setSongname(n5);
            song.setGenre(g5);
            ref.push().setValue(song);
        }
        else
        {
            i = 4;
            song_id = "https://firebasestorage.googleapis.com/v0/b/sahayak-535fb.appspot.com/o/file_example_MP3_700KB.mp3?alt=media&token=9d14ddb7-c3de-477a-b657-afb57a659f2e";
            Toast.makeText(this, "Playing Default Music.mp3", Toast.LENGTH_SHORT).show();
            t1.speak("Playing Happy Rock",TextToSpeech.QUEUE_FLUSH,null);
        }
        return song_id;
    }

    @Override
    public void onStop() {
        if (t1 != null) {
            t1.stop();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (t1 != null) {
            t1.shutdown();
        }
        super.onDestroy();
    }

    public void play_song(String s)
    {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(s);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
