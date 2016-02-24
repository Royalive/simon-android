package fr.cherp_roy.simongame;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private Button[] buttons;
    private SimonButtonClickListener listener;
    private ArrayList<Button> sequence;

    private SequencePlayTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        retrieveButtons();
        sequence = new ArrayList<>();
        sequence.add(buttons[0]);
        sequence.add(buttons[1]);
        sequence.add(buttons[2]);
        sequence.add(buttons[2]);
        sequence.add(buttons[3]);

        listener = new SimonButtonClickListener();
        task = new SequencePlayTask(this);

        for(Button btn : buttons){
            btn.setOnClickListener(listener);
        }

        playSequence(sequence);
    }

    public Button[] retrieveButtons(){

        if(buttons == null) {
            buttons = new Button[]{
                    (Button) findViewById(R.id.simon_btn1),
                    (Button) findViewById(R.id.simon_btn2),
                    (Button) findViewById(R.id.simon_btn3),
                    (Button) findViewById(R.id.simon_btn4)
            };
        }

        return buttons;
    }

    public ColorDrawable getColor(Button btn){
        return (ColorDrawable) (btn.getBackground());
    }

    public void turnOn(Button btn){
        ColorDrawable color = getColor(btn);
        color.setAlpha(255);
        btn.setBackground(color);
    }

    public void turnOff(Button btn){
        ColorDrawable color = getColor(btn);
        color.setAlpha(90);
        btn.setBackground(color);
    }

    public boolean isOn(Button btn){
        return getColor(btn).getAlpha() == 255;
    }

    public void toggle(Button btn) {
        if (isOn(btn)) {
            turnOff(btn);
        }else{
            turnOn(btn);
        }
    }

    private void playSequence(ArrayList<Button> sequence){
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
            toggle((Button) v);
        }
    }
}
