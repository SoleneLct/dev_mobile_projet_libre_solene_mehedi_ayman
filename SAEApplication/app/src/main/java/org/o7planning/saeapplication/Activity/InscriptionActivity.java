package org.o7planning.saeapplication.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.o7planning.saeapplication.Database.DataBaseProfilsManager;
import org.o7planning.saeapplication.Exception.ChampsNonRempliExecption;
import org.o7planning.saeapplication.Exception.MotdePasseDifferentException;
import org.o7planning.saeapplication.Exception.MotdePasseTropFaibleException;
import org.o7planning.saeapplication.Exception.PseudoDejaExistantException;
import org.o7planning.saeapplication.Modele.Profil;
import org.o7planning.saeapplication.R;

public class InscriptionActivity extends AppCompatActivity {

    private static final int TAILLE_MOT_DE_PASSE = 8;
    private EditText pseudo;
    private EditText nom;
    private EditText prenom;
    private EditText motDePasse;
    private EditText confirmMotDePasse;
    private Button inscriptionButton;
    private TextView lienActiviteConnection;
    protected Profil mUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        pseudo = findViewById(R.id.incription_pseudo);
        nom = findViewById(R.id.incription_nom);
        prenom = findViewById(R.id.incription_prenom);
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
                finishActivity();
            }
        });

        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inscriptionGestion();
            }
        });

    }

    private void finishActivity(){
        pseudo.setText("");
        nom.setText("");
        prenom.setText("");
        motDePasse.setText("");
        confirmMotDePasse.setText("");
        finish();
    }
    private void inscription(){
        String pseudo = this.pseudo.getText().toString();
        String nom = this.nom.getText().toString();
        String prenom = this.prenom.getText().toString();
        String motDePasse = this.motDePasse.getText().toString();
        this.mUtilisateur = new Profil(pseudo,nom,prenom,motDePasse);

        DataBaseProfilsManager db = new DataBaseProfilsManager(this);
        db.addProfils(mUtilisateur);

        Toast toast = Toast.makeText(this, "Vous êtes bien inscrit", Toast.LENGTH_SHORT);
        toast.getView().setBackgroundColor(Color.BLUE);
        TextView toastTextView = toast.getView().findViewById(android.R.id.message);
        toastTextView.setTextColor(Color.WHITE);
        toast.show();

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        finishActivity();
                    }
                },
                Toast.LENGTH_SHORT
        );
    }
    private void inscriptionGestion() {
        try {
            inscriptionValide();
            inscription();
        } catch (PseudoDejaExistantException e) {
            pseudo.setText("");
            pseudo.requestFocus();
            motDePasse.setText("");
            confirmMotDePasse.setText("");

            pseudo.setError("le pseudo choisi est déjà pris ! \nmerci d'en choisir un autre");
        } catch (ChampsNonRempliExecption e) {
            motDePasse.setText("");
            confirmMotDePasse.setText("");

            pseudo.setError("Merci de remplir tout les champs");
            pseudo.requestFocus();
            motDePasse.setError("Merci de remplir tout les champs");
            motDePasse.requestFocus();
            confirmMotDePasse.setError("Merci de remplir tout les champs");
            confirmMotDePasse.requestFocus();
        } catch (MotdePasseTropFaibleException e) {
            motDePasse.setText("");
            confirmMotDePasse.setText("");

            motDePasse.setError("Le mot de passe doit faire au moins 8 charactère");
        } catch (MotdePasseDifferentException e) {
            motDePasse.setText("");
            confirmMotDePasse.setText("");

            motDePasse.setError("Les mots de passe sont différents");
            confirmMotDePasse.setError("Les mots de passe sont différents");
        }
    }

    private void inscriptionValide() throws PseudoDejaExistantException,
            MotdePasseDifferentException, MotdePasseTropFaibleException,
            ChampsNonRempliExecption {
        if( pseudo.getText().toString().trim().isEmpty()
            || nom.getText().toString().trim().isEmpty()
            || prenom.getText().toString().trim().isEmpty()
            || motDePasse.getText().toString().trim().isEmpty()
            || confirmMotDePasse.getText().toString().trim().isEmpty())
            throw new ChampsNonRempliExecption();

        if(motDePasse.getText().toString().length() < TAILLE_MOT_DE_PASSE)
            throw new MotdePasseTropFaibleException();

        if(!motDePasse.getText().toString().equals(confirmMotDePasse.getText().toString()))
            throw new MotdePasseDifferentException();

        Profil user = new DataBaseProfilsManager(InscriptionActivity.this)
                .getProfilsByNameAndMdp(pseudo.getText().toString(),motDePasse.getText().toString());
        if(user != null)
            throw new PseudoDejaExistantException();
    }

}