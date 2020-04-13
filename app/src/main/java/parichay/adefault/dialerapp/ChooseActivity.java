package parichay.adefault.dialerapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class ChooseActivity extends AppCompatActivity {
    Button b1,b2;
    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR)
                {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        t1.speak("Press on Right side for music, on left side to call",TextToSpeech.QUEUE_FLUSH,null);



        b1 = (Button)findViewById(R.id.right);
        b2 = (Button)findViewById(R.id.left);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChooseActivity.this, Main2Activity.class);

                t1.speak("Right side pressed",TextToSpeech.QUEUE_FLUSH,null);

                startActivity(i);


            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChooseActivity.this, instruction.class);
                t1.speak("Left side pressed",TextToSpeech.QUEUE_FLUSH,null);

                startActivity(i);


            }
        });


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
}
