package fr.cherp_roy.simongame;

import android.graphics.drawable.ColorDrawable;
import android.widget.Button;


public class SimonButton {
    private Button button;
    private int tone;

    /**
     * Crée une instance d'un SimonButton, associant un bouton de la GUI à un son
     * @param button
     * @param tone
     */
    public SimonButton(Button button, int tone) {
        this.button = button;
        this.button.setTag(this);
        this.tone = tone;
    }

    /**
     * Récupère le bouton de la GUI
     * @return
     */
    public Button getButton() {
        return button;
    }

    /**
     * Récupère la couleur du bouton
     * @return
     */
    public ColorDrawable getColor(){
        return (ColorDrawable) (button.getBackground());
    }

    /**
     * Récupère la tonalité associé au bouton
     * @return
     */
    public int getTone() {
        return tone;
    }
}
