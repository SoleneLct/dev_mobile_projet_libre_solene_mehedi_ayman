package org.o7planning.saeapplication.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.o7planning.saeapplication.R;

public class ConnexionActivity extends AppCompatActivity {

    private EditText pseudo;
    private EditText motDePasse;
    private Button connexionButton;
    private TextView lienActiviteInscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pseudo = findViewById(R.id.connexion_pseudo);
        motDePasse = findViewById(R.id.connexion_mot_de_passe);
        connexionButton = findViewById(R.id.connexionButton);

        lienActiviteInscription = findViewById(R.id.lien_activite_inscription);
        lienActiviteInscription.setTextColor(Color.BLUE);
        lienActiviteInscription.setPaintFlags(lienActiviteInscription.getPaintFlags()
                | Paint.UNDERLINE_TEXT_FLAG);

        lienActiviteInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent connexionActivity = new Intent(ConnexionActivity.this, InscriptionActivity.class);
                startActivity(connexionActivity);
            }
        });

        connexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seConnecter();
            }
        });
    }

    private void seConnecter() {
        if(pseudo.getText().toString().trim().isEmpty()
                ||motDePasse.getText().toString().trim().isEmpty()){
            pseudo.setError("Merci de remplir tout les champs");
            pseudo.requestFocus();
            motDePasse.setError("Merci de remplir tout les champs");
            motDePasse.requestFocus();
        }
    }

}