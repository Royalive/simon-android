package fr.cherp_roy.simongame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

    //Constantes
    public static final String CLE_RES = "CLE_RES";
    private static final int NB_BOUTONS = 4;
    private static final int  NB_COULEUR_BASE = 4;

    private static final int PLAYING = 0;
    private static final int WON = 1;
    private static final int LOST = 2;

    //Attributs
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
    private boolean tick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        decompte = (TextView) findViewById(R.id.decompte);
        nbCouleur=NB_BOUTONS;
        cpt=0;
        level=1;
        tick=false;
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

    /**
     * Affiche le score de la partie, propose de relancer une partie ou de quitter le jeu
     */
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
                })
                .setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        cpt=0;
        level=1;
        nbCouleur=NB_COULEUR_BASE;
        state = PLAYING;

    }

    /**
     * Lance le compte à rebours de début
     */
    public void decoupteSeq()
    {
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {

                tick=true;
                Log.i("SEC : ", "seconds remaining: " + millisUntilFinished / 1000);
                decompte.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {

                tick= false;
                decompte.setText("");
                compoSequence(nbCouleur);
            }
        }.start();
    }

    /**
     * Initialise le niveau suivant en rajoutant une couleur
     */
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

    /**
     * Crée une séquence aléatoire de couleur
     * @param nbC Taille de la séquence
     */
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

    /**
     * Récupère et initialise les boutons de l'interface
     * @return Tableau de bouton de l'interface
     */
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

    /**
     * Allume un bouton et joue le son associé
     * @param btn
     */
    public void turnOn(SimonButton btn){
        ColorDrawable color = btn.getColor();
        color.setAlpha(255);
        btn.getButton().setBackground(color);
        toneGenerator.startTone(btn.getTone(), 200);
    }

    /**
     * Eteint un bouton
     * @param btn
     */
    public void turnOff(SimonButton btn) {
        ColorDrawable color = btn.getColor();
        color.setAlpha(90);
        btn.getButton().setBackground(color);
    }

    /**
     * Détermine si un bouton est allumé
     * @param btn
     * @return true si le bouton est actuellement allumé
     */
    public boolean isOn(SimonButton btn){
        return btn.getColor().getAlpha() == 255;
    }


    /**
     * Permet de jouer une séquence
     * @param sequence
     * @return true si la séquence a bien été lancée. false si une séquence est déjà en cours de lecture
     */
    private boolean playSequence(ArrayList<SimonButton> sequence){

        if (!isBusy() && !tick) {
            task = new SequencePlayTask(GameActivity.this);
            task.execute(sequence);
            return true;
        }

        return false;

    }

    /**
     * Signale la fin d'une séquence
     */
    public void onSequenceEnd() {
        task = null;
        if(state == WON){
            nextLevel();
        }else if(state == LOST){
            reGame();
        }
    }

    /**
     * Détermine si une séquence est en cours de lecture
     * @return true si une séquence est en cours de lecture
     */
    public boolean isBusy(){
        return task != null;
    }

    /**
     * Appelé lors de la destruction de l'activité
     * Annule la séquence en cours si elle existe
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null)
            task.cancel(true);
    }

    /**
     * Appelé lorsque l'activité se termine et retourne au menu
     */
    @Override
    public void finish()
    {
        Intent data =new Intent();
        data.putExtra(CLE_RES, 1);
        setResult(RESULT_OK, data);
        super.finish();
    }


    /**
     * Listener écoutant le clic sur les boutons du jeu
     */
    class SimonButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            final SimonButton sbtn = (SimonButton) v.getTag();

            if (!isBusy() && !tick) {
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
