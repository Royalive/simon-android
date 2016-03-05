package fr.cherp_roy.simongame;

import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static int NB_BOUTONS =4;
    private static int NB_COULEUR_BASE =4;

    private SimonButton[] buttons;

    private SimonButtonClickListener listener;
    private ArrayList<SimonButton> sequence;

    private ToneGenerator toneGenerator;
    private SequencePlayTask task;

    private int level;
    private int cpt;
    private int nbCouleur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        nbCouleur=NB_BOUTONS;
        cpt=0;
        level=1;

        retrieveButtons();
        listener = new SimonButtonClickListener();
        toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

        for(SimonButton sbtn : buttons){
            sbtn.getButton().setOnClickListener(listener);
            turnOff(sbtn);
        }
        compoSequence(nbCouleur);

    }

    public void reGame()
    {
        cpt=-1;
        level=1;
        nbCouleur=NB_COULEUR_BASE;

        compoSequence(NB_COULEUR_BASE);

    }

    public void nextLevel()
    {
        cpt=-1;
        level++;
        nbCouleur++;

        compoSequence(nbCouleur);
    }

    public void compoSequence(int nbC)
    {
        sequence = new ArrayList<>();
        Random r = new Random();
        task = new SequencePlayTask(this);

        for (int i = 0; i<nbC;i++)
        {
            sequence.add(buttons[r.nextInt(NB_BOUTONS)]);
        }

        playSequence(sequence);
    }

    public SimonButton[] retrieveButtons(){

        if(buttons == null) {
            buttons = new SimonButton[]{
                    new SimonButton((Button) findViewById(R.id.simon_btn1), ToneGenerator.TONE_DTMF_2),
                    new SimonButton((Button) findViewById(R.id.simon_btn2), ToneGenerator.TONE_DTMF_4),
                    new SimonButton((Button) findViewById(R.id.simon_btn3), ToneGenerator.TONE_DTMF_6),
                    new SimonButton((Button) findViewById(R.id.simon_btn4), ToneGenerator.TONE_DTMF_8),
            };
        }

        return buttons;
    }

    public void turnOn(SimonButton btn){
        ColorDrawable color = btn.getColor();
        color.setAlpha(255);
        btn.getButton().setBackground(color);
        toneGenerator.startTone(btn.getTone(), 200);
    }

    public void turnOff(SimonButton btn){
        ColorDrawable color = btn.getColor();
        color.setAlpha(90);
        btn.getButton().setBackground(color);
    }

    public boolean isOn(SimonButton btn){
        return btn.getColor().getAlpha() == 255;
    }

    public void toggle(SimonButton btn) {
        if (isOn(btn)) {
            turnOff(btn);
        }else{
            turnOn(btn);
        }
    }

    private void playSequence(ArrayList<SimonButton> sequence){
        task.execute(sequence);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task.cancel(true);
    }

    class SimonButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            final SimonButton sbtn = (SimonButton) v.getTag();
            new SequencePlayTask(GameActivity.this).execute(new ArrayList<SimonButton>(){{ add(sbtn); }});

            if (sequence.get(cpt).getColor() == sbtn.getColor())
            {
                Log.i("TEST COLOR :", "Bonne couleur");
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Perdu :(", Toast.LENGTH_SHORT).show();
                reGame();
            }

            if (cpt ==sequence.size()-1){

                Toast.makeText(getApplicationContext(), "Gagné :)", Toast.LENGTH_SHORT).show();
                nextLevel();
            }
            cpt++;
        }
    }
}
