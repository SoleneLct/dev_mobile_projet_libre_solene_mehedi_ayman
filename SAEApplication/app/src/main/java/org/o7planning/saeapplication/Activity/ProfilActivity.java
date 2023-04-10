package org.o7planning.saeapplication.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.o7planning.saeapplication.Database.DataBaseProfilsManager;
import org.o7planning.saeapplication.Exception.ChampsNonRempliExecption;
import org.o7planning.saeapplication.Exception.PseudoDejaExistantException;
import org.o7planning.saeapplication.Modele.Profil;
import org.o7planning.saeapplication.R;

public class ProfilActivity extends AppCompatActivity {

    public static final int RESULT_CODE_ACTIVITY = 3;

    private static final int TAILLE_MOT_DE_PASSE = 8;
    public static final String MODIFICATION_INTENT_EXTRA = "MODIFICATION_INTENT_EXTRA";
    private static boolean MODIFICATION = false;
    private TextView pseudo;
    private EditText nom;
    private EditText prenom;
    private Button saveButton;
    private TextView quitte;
    private ImageButton profilImage;
    protected Profil mUtilisateur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profils);
        mUtilisateur = (Profil) getIntent().getSerializableExtra("userObject");

        pseudo = findViewById(R.id.profil_pseudo);
        nom = findViewById(R.id.profil_nom_input);
        prenom = findViewById(R.id.profil_prenom_input);
        profilImage = findViewById(R.id.profil_image);

        pseudo.setText(mUtilisateur.getPseudo());
        nom.setText(mUtilisateur.getNom());
        prenom.setText(mUtilisateur.getPrenom());
        if(mUtilisateur.getPhoto() != null)
        {
            Bitmap bm = BitmapFactory.decodeByteArray(mUtilisateur.getPhoto(), 0, mUtilisateur.getPhoto().length);
            profilImage.setImageBitmap(bm);
        }
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
        Intent intent = new Intent();
        intent.putExtra(MODIFICATION_INTENT_EXTRA,MODIFICATION);
        setResult(RESULT_OK, intent);
        nom.setText("");
        prenom.setText("");
        finish();
    }

    private void modificationGestion() {
        try {
            String nom = this.nom.getText().toString().trim();
            String prenom = this.prenom.getText().toString().trim();

            if( nom.equals(mUtilisateur.getNom().trim()) && prenom.equals(mUtilisateur.getPrenom().trim())){
                Toast toast = Toast.makeText(this, "Vous quitter SANS modifiction", Toast.LENGTH_SHORT);
                toast.show();

                this.MODIFICATION = false;
            }else {
                modificationValide();

                DataBaseProfilsManager db = new DataBaseProfilsManager(this);

                mUtilisateur.setNom(this.nom.getText().toString().trim());
                mUtilisateur.setPrenom(this.prenom.getText().toString().trim());

                db.updateProfil(mUtilisateur);

                Toast toast = Toast.makeText(this, "Vous quitter AVEC modifiction", Toast.LENGTH_SHORT);
                toast.show();

                this.MODIFICATION = true;

                finishActivity();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishActivity();
                }
            }, Toast.LENGTH_SHORT);
        } catch (PseudoDejaExistantException e) {
            pseudo.setText("");
            pseudo.requestFocus();
            pseudo.setError("le pseudo choisi est déjà pris ! \nmerci d'en choisir un autre");
        } catch (ChampsNonRempliExecption e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Modification impossible", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void modificationValide() throws PseudoDejaExistantException,
            ChampsNonRempliExecption {
        if(pseudo.getText().toString().trim().isEmpty()
            || nom.getText().toString().trim().isEmpty()
            || prenom.getText().toString().trim().isEmpty()){
            if(nom.getText().toString().trim().isEmpty())
                nom.setError("Vous avez modifier merci de remplir ce champs");
            if(pseudo.getText().toString().trim().isEmpty())
                pseudo.setError("Vous avez modifier merci de remplir ce champs");
            if(prenom.getText().toString().trim().isEmpty())
                prenom.setError("Vous avez modifier merci de remplir ce champs");
            throw new ChampsNonRempliExecption();
        }

        if(!pseudo.getText().toString().trim().equals(mUtilisateur.getPseudo())) {
            Profil user = new DataBaseProfilsManager(ProfilActivity.this)
                    .getProfilsByNameAndMdp(pseudo.getText().toString(), mUtilisateur.getMot_de_passe());
            if (user != null)
                throw new PseudoDejaExistantException();
        }
    }
}