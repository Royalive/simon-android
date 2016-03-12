package fr.cherp_roy.simongame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //Constantes
    public static final int REQUETE = 1;

    //Attributs
    private Intent intSet;
    private Button btnJouer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intSet = new Intent(this, GameActivity.class);
        btnJouer = (Button)findViewById(R.id.jouer);

        btnJouer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                startActivityForResult(intSet, REQUETE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUETE && resultCode == RESULT_OK) {
            Log.d("BACK VUE", "OK");
        }
    }

}
