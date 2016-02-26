package fr.cherp_roy.simongame;

import android.graphics.drawable.ColorDrawable;
import android.widget.Button;

/**
 * Created by aurelien on 25/02/2016.
 */
public class SimonButton {
    private Button button;
    private int tone;

    public SimonButton(Button button, int tone) {
        this.button = button;
        this.button.setTag(this);
        this.tone = tone;
    }

    public Button getButton() {
        return button;
    }

    public ColorDrawable getColor(){
        return (ColorDrawable) (button.getBackground());
    }

    public int getTone() {
        return tone;
    }
}
