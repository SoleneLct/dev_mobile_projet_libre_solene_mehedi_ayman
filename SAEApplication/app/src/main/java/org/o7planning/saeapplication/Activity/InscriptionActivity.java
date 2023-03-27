package org.o7planning.saeapplication.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.o7planning.sae_dev.Exception.ChampsNonRempliExecption;
import org.o7planning.sae_dev.Exception.MotdePasseDifferentException;
import org.o7planning.sae_dev.Exception.MotdePasseTropFaibleException;
import org.o7planning.sae_dev.Exception.pseudoDejaExistantException;
import org.o7planning.sae_dev.Modele.Profils;
import org.o7planning.saeapplication.R;

public class InscriptionActivity extends AppCompatActivity {

    private static final int TAILLE_MOT_DE_PASSE = 8;
    private EditText pseudo;
    private EditText motDePasse;
    private EditText confirmMotDePasse;
    private Button inscriptionButton;
    private TextView lienActiviteConnection;
    protected Profils mUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        pseudo = findViewById(R.id.incription_pseudo);
        motDePasse = findViewById(R.id.incription_mot_de_passe);
        confirmMotDePasse = findViewById(R.id.confirme_mot_de_passe);
        inscriptionButton = findViewById(R.id.inscriptionButton);
        lienActiviteConnection = findViewById(R.id.lien_activite_connexion);
        lienActiviteConnection.setTextColor(Color.BLUE);
        lienActiviteConnection.setPaintFlags(lienActiviteConnection.getPaintFlags()
                | Paint.UNDERLINE_TEXT_FLAG);

        lienActiviteConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent connexionActivity = new Intent(InscriptionActivity.this,
                        ConnexionActivity.class);
                startActivity(connexionActivity);
            }
        });

        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inscrire();
            }
        });

    }

    private void inscrire() {
        try {
            inscriptionValide();
            mUtilisateur = new Profils(pseudo.getText().toString());
            Toast.makeText(this, "Vous être bien inscrit!",
                    Toast.LENGTH_LONG).show();
        } catch (pseudoDejaExistantException e) {
            pseudo.setError("le pseudo choisi est déjà pris ! \nmerci d'en choisir un autre");
        } catch (ChampsNonRempliExecption e) {
            pseudo.setError("Merci de remplir tout les champs");
            pseudo.requestFocus();
            motDePasse.setError("Merci de remplir tout les champs");
            motDePasse.requestFocus();
            confirmMotDePasse.setError("Merci de remplir tout les champs");
            confirmMotDePasse.requestFocus();
        } catch (MotdePasseTropFaibleException e) {
            motDePasse.setError("Le mot de passe doit faire au moins 8 charactère");
        } catch (MotdePasseDifferentException e) {
            motDePasse.setError("Les mots de passe sont différents");
            confirmMotDePasse.setError("Les mots de passe sont différents");
        }
    }

    private void inscriptionValide() throws pseudoDejaExistantException,
            MotdePasseDifferentException, MotdePasseTropFaibleException,
            ChampsNonRempliExecption {

        if(pseudo.getText().toString().trim().isEmpty()
                ||motDePasse.getText().toString().trim().isEmpty()
                ||confirmMotDePasse.getText().toString().trim().isEmpty())
            throw new ChampsNonRempliExecption();
        if(motDePasse.getText().toString().length() < TAILLE_MOT_DE_PASSE)
            throw new MotdePasseTropFaibleException();
        if
        (!motDePasse.getText().toString().equals(confirmMotDePasse.getText().toString()))
        {
            throw new MotdePasseDifferentException();
        }
    }

}