package org.o7planning.saeapplication.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.o7planning.saeapplication.Database.DataBaseProfilsManager;
import org.o7planning.saeapplication.Modele.Profil;
import org.o7planning.saeapplication.R;

public class MainActivity extends AppCompatActivity {

    private EditText pseudo;
    private EditText motDePasse;
    private Button connexionButton;
    private TextView lienActiviteInscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pseudo = (EditText) findViewById(R.id.connexion_pseudo);
        motDePasse = (EditText) findViewById(R.id.connexion_mot_de_passe);
        connexionButton = (Button) findViewById(R.id.connexionButton);

        lienActiviteInscription = (TextView) findViewById(R.id.lien_activite_inscription);
        lienActiviteInscription.setTextColor(Color.BLUE);
        lienActiviteInscription.setPaintFlags(lienActiviteInscription.getPaintFlags()
                | Paint.UNDERLINE_TEXT_FLAG);

        lienActiviteInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent connexionActivity = new Intent(MainActivity.this, InscriptionActivity.class);
                startActivity(connexionActivity);
            }
        });

        connexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(seConnecter()){
                    String ps = pseudo.getText().toString().trim();
                    String mdp = motDePasse.getText().toString().trim();
                    Profil user = new DataBaseProfilsManager(MainActivity.this).getProfilsByNameAndMdp(ps,mdp);

                    if(user != null){
                        Intent imageFolderActivity = new Intent(MainActivity.this, ImageFolderActivity.class);
                        imageFolderActivity.putExtra("userObject" ,user);
                        startActivity(imageFolderActivity);
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Utilisateur inconnu Vous inscrire ?", Toast.LENGTH_SHORT);
                        toast.show();
                        pseudo.setError("Utilisateur non trouvé\nInscrivez vous !");
                        pseudo.requestFocus();
                        motDePasse.setText("");
                    }
                }
            }
        });
    }

    private Boolean seConnecter() {
        Boolean condition = true;
        if(pseudo.getText().toString().trim().isEmpty()){
            pseudo.setError("Merci de remplir tout les champs");
            pseudo.requestFocus();
            condition = false;
        }
        if (motDePasse.getText().toString().trim().isEmpty()) {
            motDePasse.setError("Merci de remplir tout les champs");
            motDePasse.requestFocus();
            condition = false;
        }
        return condition;
    }
}
