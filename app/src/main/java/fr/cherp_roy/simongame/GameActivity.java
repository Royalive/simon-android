package fr.cherp_roy.simongame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static int NB_BOUTONS = 4;
    private static int NB_COULEUR_BASE = 4;

    private static final int PLAYING = 0;
    private static final int WON = 1;
    private static final int LOST = 2;


    private SimonButton[] buttons;

    private SimonButtonClickListener listener;
    private ArrayList<SimonButton> sequence;

    private ToneGenerator toneGenerator;
    private SequencePlayTask task;

    private int level;
    private int state;
    private int cpt;
    private int nbCouleur;
    private TextView decompte;

    private long milli;
    private long sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        decompte = (TextView) findViewById(R.id.decompte);
        nbCouleur=NB_BOUTONS;
        cpt=0;
        level=1;
        state = PLAYING;

        retrieveButtons();
        listener = new SimonButtonClickListener();
        toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

        for(SimonButton sbtn : buttons){
            sbtn.getButton().setOnClickListener(listener);
            turnOff(sbtn);
        }

        decoupteSeq();

    }

    public void reGame()
    {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GameActivity.this);
        alertDialogBuilder.setTitle("Rejouer ?")
                .setCancelable(false)
                .setMessage("Vous avez atteint le niveau " + level)
                .setPositiveButton("Rejouer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        decoupteSeq();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        cpt=0;
        level=1;
        nbCouleur=NB_COULEUR_BASE;
        state = PLAYING;

    }
    public void decoupteSeq()
    {
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.i("SEC : ", "seconds remaining: " + millisUntilFinished / 1000);
                decompte.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {

                decompte.setText("");
                compoSequence(nbCouleur);
            }
        }.start();
    }

    public void nextLevel()
    {
        cpt=0;
        level++;
        nbCouleur++;
        state = PLAYING;

        Random r = new Random();
        sequence.add(buttons[r.nextInt(NB_BOUTONS)]);

        playSequence(sequence);
    }

    public void compoSequence(int nbC)
    {
        sequence = new ArrayList<>();
        Random r = new Random();

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

    private boolean playSequence(ArrayList<SimonButton> sequence){

        Log.d("SIMON", "playSequence : " + isBusy());
        if(!isBusy()) {
            task = new SequencePlayTask(GameActivity.this);
            task.execute(sequence);
            return true;
        }

        return false;

    }

    public void onSequenceEnd() {
        task = null;
        if(state == WON){
            nextLevel();
        }else if(state == LOST){
            reGame();
        }
    }

    public boolean isBusy(){
        return task != null;
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

            if (!isBusy()) {
                playSequence(new ArrayList<SimonButton>() {{
                    add(sbtn);
                }});

                if (sequence.get(cpt).getColor() == sbtn.getColor()) {
                    Log.i("TEST COLOR :", "Bonne couleur");
                } else {
                    Toast.makeText(getApplicationContext(), "Perdu :(", Toast.LENGTH_SHORT).show();
                    state = LOST;
                    return;
                }

                if (cpt == sequence.size() - 1) {

                    Toast.makeText(getApplicationContext(), "Gagné :)", Toast.LENGTH_SHORT).show();
                    state = WON;
                }else{
                    cpt++;
                }

            }
        }
    }
}
