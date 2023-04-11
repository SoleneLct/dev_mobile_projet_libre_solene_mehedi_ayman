package org.o7planning.saeapplication.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.o7planning.saeapplication.Database.DataBaseProfilsManager;
import org.o7planning.saeapplication.Exception.ChampsNonRempliExecption;
import org.o7planning.saeapplication.Exception.MotdePasseDifferentException;
import org.o7planning.saeapplication.Exception.MotdePasseTropFaibleException;
import org.o7planning.saeapplication.Exception.PseudoDejaExistantException;
import org.o7planning.saeapplication.Modele.Image;
import org.o7planning.saeapplication.Modele.Profil;
import org.o7planning.saeapplication.R;

public class InscriptionActivity extends AppCompatActivity {
    public static final int RESULT_CODE_ACTIVITY = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;

    private static final int TAILLE_MOT_DE_PASSE = 8;
    private EditText pseudo;
    private EditText nom;
    private EditText prenom;
    private EditText motDePasse;
    private EditText confirmMotDePasse;
    private ImageButton addImage;
    private Button inscriptionButton;
    private TextView lienActiviteConnection;
    protected Profil mUtilisateur;
    private byte[] photo = null;

    private void finishActivity(){
        pseudo.setText("");
        nom.setText("");
        prenom.setText("");
        motDePasse.setText("");
        confirmMotDePasse.setText("");
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        this.pseudo = findViewById(R.id.incription_pseudo);
        this.nom = findViewById(R.id.incription_nom);
        this.prenom = findViewById(R.id.incription_prenom);
        this.motDePasse = findViewById(R.id.incription_mot_de_passe);
        this.confirmMotDePasse = findViewById(R.id.confirme_mot_de_passe);
        this.inscriptionButton = findViewById(R.id.inscriptionButton);
        this.addImage = findViewById(R.id.addImage);
        this.lienActiviteConnection = findViewById(R.id.lien_activite_connexion);
        this.lienActiviteConnection.setTextColor(Color.BLUE);
        this.lienActiviteConnection.setPaintFlags(lienActiviteConnection.getPaintFlags()
                | Paint.UNDERLINE_TEXT_FLAG);
        this.lienActiviteConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });
        this.inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inscriptionGestion();
            }
        });

        this.addImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 prendrePhoto(v);
             }
         }
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

    private void inscription(){
        String pseudo = this.pseudo.getText().toString().trim();
        String nom = this.nom.getText().toString().trim();
        String prenom = this.prenom.getText().toString().trim();
        String motDePasse = this.motDePasse.getText().toString();
        this.mUtilisateur = new Profil(pseudo,nom,prenom,motDePasse,photo);

        DataBaseProfilsManager db = new DataBaseProfilsManager(this);
        db.addProfils(mUtilisateur);
        Toast toast = Toast.makeText(this, "Vous êtes bien inscrit", Toast.LENGTH_SHORT);
        toast.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finishActivity();
            }
        }, Toast.LENGTH_SHORT);
    }

    public void prendrePhoto(View view) {
        // Vérifier si l'application a l'autorisation d'accéder à la caméra
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Si l'autorisation n'est pas encore accordée, on demande la permission
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // L'autorisation est déjà accordée, on peut lancer l'activité de la caméra
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                } else {
                    Toast.makeText(this, "La permission de la caméra a été refusée", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    this.photo = Image.bitmapToByteArray(imageBitmap);
                    this.addImage.setImageBitmap(imageBitmap);
                    break;
                default:
                    Toast.makeText(this, "result code no found", Toast.LENGTH_SHORT).show();
            }
        }
    }

}