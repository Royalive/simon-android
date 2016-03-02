package fr.cherp_roy.simongame;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

public class SequencePlayTask extends AsyncTask<ArrayList<SimonButton>, SequencePlayTask.ButtonAction, Void> {

    public static boolean TURN_OFF = false;
    public static boolean TURN_ON = true;

    private GameActivity activity;

    public SequencePlayTask(GameActivity activity){
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(ArrayList<SimonButton>... seqs) {

        ArrayList<SimonButton> sequence = seqs[0];

        try {

            Log.d("SEQUENCEPLAY", "Allumage");

            for(SimonButton b : sequence){

                publishProgress(new ButtonAction(b, TURN_ON));
                Thread.sleep(700);
                publishProgress(new ButtonAction(b, TURN_OFF));
                Thread.sleep(200);
                Log.d("SEQUENCEPLAY", "Allumage");

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(ButtonAction... values) {
        ButtonAction ba = values[0];

        if(ba.action == TURN_ON){
            activity.turnOn(ba.button);
        }else{
            activity.turnOff(ba.button);
        }
    }


    public class ButtonAction{
        public SimonButton button;
        public boolean action;

        public ButtonAction(SimonButton button, boolean action){
            this.button = button;
            this.action = action;
        }
    }

    @Override
    protected void onCancelled() {
        Log.d("SEQUENCEPLAY", "Annulation");
        super.onCancelled();
    }
}
