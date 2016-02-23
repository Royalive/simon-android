package fr.cherp_roy.simongame;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {

    private Button[] buttons;
    private SimonButtonClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        retrieveButtons();
        listener = new SimonButtonClickListener();

        for(Button btn : buttons){
            btn.setOnClickListener(listener);
        }
    }

    private void retrieveButtons(){
        buttons = new Button[]{
                (Button) findViewById(R.id.simon_btn1),
                (Button) findViewById(R.id.simon_btn2),
                (Button) findViewById(R.id.simon_btn3),
                (Button) findViewById(R.id.simon_btn4)
        };
    }

    private ColorDrawable getColor(Button btn){
        return (ColorDrawable) (btn.getBackground());
    }

    private void turnOn(Button btn){
        ColorDrawable color = getColor(btn);
        color.setAlpha(255);
        btn.setBackground(color);
    }

    private void turnOff(Button btn){
        ColorDrawable color = getColor(btn);
        color.setAlpha(90);
        btn.setBackground(color);
    }

    private boolean isOn(Button btn){
        return getColor(btn).getAlpha() == 255;
    }

    private void toggle(Button btn) {
        if (isOn(btn)) {
            turnOff(btn);
        }else{
            turnOn(btn);
        }
    }

    class SimonButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            toggle((Button) v);
        }
    }
}
