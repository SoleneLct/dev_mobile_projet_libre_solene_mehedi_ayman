package org.o7planning.saeapplication.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.o7planning.saeapplication.Activity.Other.CreateFolderWithApiActivity;
import org.o7planning.saeapplication.Database.DataBaseFolderImageManager;
import org.o7planning.saeapplication.Database.DataBaseImageManager;
import org.o7planning.saeapplication.Modele.Folder;
import org.o7planning.saeapplication.Modele.Image;
import org.o7planning.saeapplication.Modele.Profil;
import org.o7planning.saeapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StoreImageActivity extends AppCompatActivity {

    public static final int RESULT_CODE_ACTIVITY = 4;
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;
    private ImageButton image;
    private Button saveAndStore,createFirstFolderButton;
    private TextView quitte;
    private EditText nom;
    private Spinner spinner;
    private byte[] mPhoto;
    private Profil mUtilisateur;
    private Folder mFolder;
    private boolean second = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_new_image);
        this.mUtilisateur = (Profil) getIntent().getSerializableExtra("userObject");

        // Récupération des vues déclarées dans le layout XML
        image = findViewById(R.id.image);
        nom = findViewById(R.id.image_nom_input);
        spinner = findViewById(R.id.spinner);
        createFirstFolderButton = findViewById(R.id.create_first_folder);
        refreshFolder();

        // Ajout d'un listener pour le clic sur l'image pour prendre une photo
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prendrePhoto(v);
            }
        });

        // Ajout d'un listener pour le bouton "Enregistrer et stocker"
        saveAndStore = findViewById(R.id.save_and_store);
        saveAndStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (imageValide()){
                    DataBaseImageManager db = new DataBaseImageManager(StoreImageActivity.this);
                    Image image = new Image(nom.getText().toString().trim(), mPhoto,mFolder.getId(),mUtilisateur.getId());
                    db.addImage(image);
                    Toast toast = Toast.makeText(StoreImageActivity.this, "Votre image à bien été sauvegarder", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
            }
        });

        quitte = findViewById(R.id.exit);
        quitte.setTextColor(Color.RED);
        quitte.setPaintFlags(quitte.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        quitte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
                    this.mPhoto = Image.bitmapToByteArray(imageBitmap);
                    this.image.setImageBitmap(imageBitmap);
                    break;
                case CreateFolderWithApiActivity.RESULT_CODE_ACTIVITY:
//                    Boolean hello = data.getBooleanExtra(ViewProfilActivity.MODIFICATION_INTENT_EXTRA,false);
                    refreshFolder();
                    break;
                default:
                    Toast.makeText(this, "result code no found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void refreshFolder() {
        DataBaseFolderImageManager dbFolder = new DataBaseFolderImageManager(StoreImageActivity.this);
        List<String> folderNames = new ArrayList<>();
        folderNames.addAll(dbFolder
                .getAllFolderImagesByUserId(mUtilisateur.getId())
                .stream()
                .map(x->x.getTitle())
                .collect(Collectors.toList()));
        folderNames.add(0,"Ajouter un nouveaux dossier");
        if(folderNames.size() > 1){
            createFirstFolderButton.setVisibility(View.INVISIBLE);
            spinner.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, folderNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(1);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position == 0){
                        if(mPhoto == null){
                            Toast.makeText(StoreImageActivity.this, "Veuillez au moin prendre un photo", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent profilActivity = new Intent(StoreImageActivity.this, CreateFolderWithApiActivity.class);
                            profilActivity.putExtra("userObject" , mUtilisateur);
                            profilActivity.putExtra("photObject" , mPhoto);
                            startActivityForResult(profilActivity, CreateFolderWithApiActivity.RESULT_CODE_ACTIVITY);
                        }
                    }else if(position!=-1){
                        String selectedFolderName = (String) parent.getItemAtPosition(position);
                        DataBaseFolderImageManager dbFolder = new DataBaseFolderImageManager(StoreImageActivity.this);
                        mFolder = dbFolder.getFolderImage(mUtilisateur.getId(),selectedFolderName);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Code à exécuter lorsque rien n'est sélectionné
                }
            });
        }
        else {
            createFirstFolderButton.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.INVISIBLE);
            createFirstFolderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent profilActivity = new Intent(StoreImageActivity.this, CreateFolderWithApiActivity.class);
                    profilActivity.putExtra("userObject" , mUtilisateur);
                    profilActivity.putExtra("photObject" , mPhoto);
                    startActivityForResult(profilActivity, CreateFolderWithApiActivity.RESULT_CODE_ACTIVITY);
                }
            });
        }
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

    private boolean imageValide() {
        if(mPhoto == null ){
            Toast toast = Toast.makeText(this, "Veuiller fournir une photo", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if(nom.getText().toString().trim().isEmpty()){
            Toast toast = Toast.makeText(this, "Veuiller fournir un nom à cette image", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        DataBaseImageManager db = new DataBaseImageManager(StoreImageActivity.this);
        if(mFolder == null) {
            Toast toast = Toast.makeText(this, "Veuiller selection un dossier", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }
}
