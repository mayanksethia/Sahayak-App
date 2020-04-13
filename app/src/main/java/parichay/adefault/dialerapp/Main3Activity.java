package parichay.adefault.dialerapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class Main3Activity extends AppCompatActivity {

    private TextView txtSpeechInput;
    TextToSpeech t1;
    private Button b1;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String n1,n2,n3,n4,n5;
    String g1,g2,g3,g4,g5;
    int c1=0,c2=0,c3=0,c4=0,c5=0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;
    Song song;
    MediaPlayer mediaPlayer;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR)
                {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        song = new Song();
        ref = FirebaseDatabase.getInstance().getReference().child("Song");

        b1 = (Button) findViewById(R.id.speak);
        txtSpeechInput = (TextView)findViewById(R.id.textView14);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                }
                promptSpeechInput();
            }
        });

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


    }




    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            promptSpeechInput();
            return true;

        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            promptSpeechInput();
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
    String play_id;
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    String sn = result.get(0);
                    Toast.makeText(this, sn, Toast.LENGTH_SHORT).show();
                    play_id = Song_Identifier(result.get(0));
                    play_song(play_id);
                }
                break;
            }

        }
    }
    int ind=1;
    public String song_id;

    public String Song_Identifier(String song_name)
    {
        Toast.makeText(this, "the song name is: " + song_name, Toast.LENGTH_SHORT).show();
        if(song_name.equals("BachGavotteShort"))
        {

            song_id = "https://firebasestorage.googleapis.com/v0/b/team-sahayak.appspot.com/o/BachGavotteShort.mp3?alt=media&token=e725759d-d868-4476-9acc-c0baba80ad5c";
            Toast.makeText(this, "Playing BachGavotteShort.mp3", Toast.LENGTH_SHORT).show();
            n1 = "BachGavotteShort";
            g1 = "Jazz";
            t1.speak("Playing BachGavotteShort", TextToSpeech.QUEUE_FLUSH,null);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(0));
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, n1);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, g1);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            String v1 = String.valueOf(c1++);

            song.setSongname(n1);
            song.setGenre(g1);
            ref.push().setValue(song);

        }
        else if(song_name.equals("Happy") || song_name.equals("happy"))
        {
            song_id = "https://firebasestorage.googleapis.com/v0/b/team-sahayak.appspot.com/o/bensound-happyrock.mp3?alt=media&token=b9ba1557-6940-4c4f-9ef3-90468a5fbc00";
            n2 = "Happy Rock";
            g2 = "Rock";

            Toast.makeText(this, "Playing HappyRock", Toast.LENGTH_SHORT).show();
            t1.speak("Playing Happy Rock",TextToSpeech.QUEUE_FLUSH,null);
            String v2 = String.valueOf(c2++);
            song.setSongname(n2);
            song.setGenre(g2);
            ref.push().setValue(song);
        }
        else if(song_name.equals("summer") || song_name.equals("Summer"))
        {
            song_id = "https://firebasestorage.googleapis.com/v0/b/team-sahayak.appspot.com/o/bensound-summer.mp3?alt=media&token=d91c680b-8120-4b14-93d9-50e0d5e9fcaa";
            n3 = "Summer";
            g3 = "Popular Music";
            Toast.makeText(this, "Playing Summer.mp3", Toast.LENGTH_SHORT).show();
            t1.speak("Playing Summer",TextToSpeech.QUEUE_FLUSH,null);

            song.setSongname(n3);
            song.setGenre(g3);
            ref.push().setValue(song);
        }
        else if(song_name.equalsIgnoreCase("ukelele")) {
            song_id = "https://firebasestorage.googleapis.com/v0/b/team-sahayak.appspot.com/o/bensound-ukulele.mp3?alt=media&token=3b3b0749-68d7-41de-90cc-9acc8450e38c";
            n4 = "Ukelele";
            g4 = "Funk Music";



            Toast.makeText(this, "Playing Ukulele.mp3", Toast.LENGTH_SHORT).show();
            t1.speak("Playing Ukulele",TextToSpeech.QUEUE_FLUSH,null);
            song.setSongname(n4);
            song.setGenre(g4);
            ref.push().setValue(song);
        }
        else if(song_name.equals("Masakali") || song_name.equals("masakali") || song_name.equals("MASAKALI"))
        {
            song_id = "https://firebasestorage.googleapis.com/v0/b/team-sahayak.appspot.com/o/file_example_MP3_700KB.mp3?alt=media&token=d5a60392-232a-429d-9885-6a039b88f714";

            n5 = "Masakali";
            g5 = "Bollywood";

            Toast.makeText(this, "Playing Masakali", Toast.LENGTH_SHORT).show();
            t1.speak("Playing Masakali",TextToSpeech.QUEUE_FLUSH,null);

            song.setSongname(n5);
            song.setGenre(g5);
            ref.push().setValue(song);
        }
        else
        {

            song_id = "https://firebasestorage.googleapis.com/v0/b/sahayak-535fb.appspot.com/o/file_example_MP3_700KB.mp3?alt=media&token=9d14ddb7-c3de-477a-b657-afb57a659f2e";
            Toast.makeText(this, "Song not found", Toast.LENGTH_SHORT).show();
            t1.speak("Song not found, instead playing happy rock",TextToSpeech.QUEUE_FLUSH,null);
        }
        return song_id;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop(); // or mp.pause();
            mediaPlayer.release();
        }
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
