package fr.cherp_roy.simongame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_LEVEL = "EXTRA_LEVEL";
    public static final int REQUETE = 1;
    public static final String CLE_RES = "CLE_RES";

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
                startActivity(intSet);
            }
        });

    }



}
