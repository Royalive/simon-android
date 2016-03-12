package fr.cherp_roy.simongame;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Tâche asynchrone dont le rôle est de jouer une séquence de bouton.
 * C'est à dire, allumer puis éteindre successivement une série de boutons dans un ordre donné
 */
public class SequencePlayTask extends AsyncTask<ArrayList<SimonButton>, SequencePlayTask.ButtonAction, Void> {

    public static boolean TURN_OFF = false;
    public static boolean TURN_ON = true;

    private GameActivity activity;

    public SequencePlayTask(GameActivity activity){
        this.activity = activity;
    }

    /**
     * Joue la séquence passée en paramètre
     * @param seqs
     * @return
     */
    @Override
    protected Void doInBackground(ArrayList<SimonButton>... seqs) {

        ArrayList<SimonButton> sequence = seqs[0];

        try {
            for(SimonButton b : sequence){

                publishProgress(new ButtonAction(b, TURN_ON));
                Thread.sleep(500);
                publishProgress(new ButtonAction(b, TURN_OFF));
                Thread.sleep(100);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Execute dans le Thread principale, l'action d'alluamge ou d'extinction passée en paramètre
     * @param values
     */
    @Override
    protected void onProgressUpdate(ButtonAction... values) {
        ButtonAction ba = values[0];

        if(ba.action == TURN_ON){
            activity.turnOn(ba.button);
        }else{
            activity.turnOff(ba.button);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        activity.onSequenceEnd();
    }


    @Override
    protected void onCancelled() {
        Log.d("SEQUENCEPLAY", "Annulation");
        super.onCancelled();
    }

    /**
     * Représente un ordre d'allumage ou d'extinction d'un bouton.
     * Cet ordre est émis par la tâche asynchrone et sera traité dans le Thread principal
     */
    public class ButtonAction{
        public SimonButton button;
        public boolean action;

        public ButtonAction(SimonButton button, boolean action){
            this.button = button;
            this.action = action;
        }
    }
}
