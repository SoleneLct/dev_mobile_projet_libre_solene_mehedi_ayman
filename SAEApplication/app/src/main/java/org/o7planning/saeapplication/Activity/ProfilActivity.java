package org.o7planning.saeapplication.Activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
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

public class ProfilActivity extends AppCompatActivity {

    private static final int TAILLE_MOT_DE_PASSE = 8;
    private TextView pseudo;
    private EditText nom;
    private EditText prenom;
    private Button saveButton;
    private TextView quitte;
    protected Profil mUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profils);
        mUtilisateur = (Profil) getIntent().getSerializableExtra("userObject");

        pseudo = findViewById(R.id.profil_pseudo);
        nom = findViewById(R.id.profil_nom_input);
        prenom = findViewById(R.id.profil_prenom_input);

        pseudo.setText(mUtilisateur.getPseudo());
        nom.setText(mUtilisateur.getNom());
        prenom.setText(mUtilisateur.getPrenom());

        saveButton = findViewById(R.id.save_or_quit_button);
        quitte = findViewById(R.id.exit);
        quitte.setTextColor(Color.BLUE);
        quitte.setPaintFlags(quitte.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        quitte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificationGestion();
            }
        });

    }

    private void finishActivity(){
        pseudo.setText("");
        nom.setText("");
        prenom.setText("");
        finish();
    }

    private void modificationGestion() {
        try {
            if(nom.getText().toString() != mUtilisateur.getNom()
                || prenom.getText().toString() != mUtilisateur.getPrenom()){
                modificationValide();
            }else {
                Toast toast = Toast.makeText(this, "Vous quitter sans modifiction", Toast.LENGTH_SHORT);
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
        } catch (PseudoDejaExistantException e) {
            pseudo.setText("");
            pseudo.requestFocus();
            pseudo.setError("le pseudo choisi est déjà pris ! \nmerci d'en choisir un autre");
        } catch (ChampsNonRempliExecption e) {
            pseudo.setError("Vous avez modifier merci de remplir ce champs");
            pseudo.requestFocus();
        }
    }

    private void modificationValide() throws PseudoDejaExistantException,
            ChampsNonRempliExecption {
        if(pseudo.getText().toString().trim().isEmpty()
                || nom.getText().toString().trim().isEmpty()
                || prenom.getText().toString().trim().isEmpty())
            throw new ChampsNonRempliExecption();

        Profil user = new DataBaseProfilsManager(ProfilActivity.this)
                .getProfilsByNameAndMdp(pseudo.getText().toString(),mUtilisateur.getMot_de_passe().toString());
        if(user != null)
            throw new PseudoDejaExistantException();
    }
}