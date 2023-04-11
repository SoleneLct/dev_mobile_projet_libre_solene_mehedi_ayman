package org.o7planning.saeapplication.Activity.Other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.o7planning.saeapplication.Database.DataBaseFolderImageManager;
import org.o7planning.saeapplication.Modele.Folder;
import org.o7planning.saeapplication.Modele.Profil;
import org.o7planning.saeapplication.R;
import org.o7planning.saeapplication.Service.ImageApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateFolderWithApiActivity extends AppCompatActivity {

    public static final int RESULT_CODE_ACTIVITY = 5;
    public static final String MODIFICATION_INTENT_EXTRA = "MODIFICATION_INTENT_EXTRA";
    private static boolean MODIFICATION = false;

    private byte[] mPhoto;
    private Profil mUtilisateur;

    private EditText folderNameInput;
    private Button mCancelButton;
    private Button mCreateButton;
    private Spinner mSpinnerProposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_folder_api);
        mUtilisateur = (Profil) getIntent().getSerializableExtra("userObject");
        mPhoto = (byte[]) getIntent().getSerializableExtra("photObject");

        mCancelButton = findViewById(R.id.cancel_button);
        mCreateButton = findViewById(R.id.create_button);
        mSpinnerProposition = findViewById(R.id.spinner_proposition);
        folderNameInput = findViewById(R.id.folder_name_input);
        // Ajouter des listeners pour les boutons si nécessaire
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createImageFolder();
            }
        });

        List<String> folderNames = new ArrayList<>();
        try {
            folderNames = ImageApiService.getImageInfo(mPhoto,CreateFolderWithApiActivity.this).keySet().stream().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, folderNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerProposition.setAdapter(adapter);
        mSpinnerProposition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFolderName = (String) parent.getItemAtPosition(position);
                folderNameInput.setText(selectedFolderName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Code à exécuter lorsque rien n'est sélectionné
            }
        });
    }
    private void finishActivity(){
        Intent intent = new Intent();
        intent.putExtra(MODIFICATION_INTENT_EXTRA,MODIFICATION);
        setResult(RESULT_OK, intent);
        folderNameInput.setText("");
        finish();
    }
    private void createImageFolder(){
        if (folderNameInput.getText().toString().trim().isEmpty()) {
            folderNameInput.setError("Merci de remplir le nom choisir un autre dossier");
            folderNameInput.requestFocus();
        }else {
            DataBaseFolderImageManager db = new DataBaseFolderImageManager(CreateFolderWithApiActivity.this);
            Folder folder = db.getFolderImage(mUtilisateur.getId(),
                    folderNameInput.getText().toString());
            if(folder == null){
                folder = new Folder(folderNameInput.getText().toString(),mUtilisateur.getId()) ;
                db.addFolderImage(folder);
                Toast toast = Toast.makeText(CreateFolderWithApiActivity.this, "Dossier ajouter !", Toast.LENGTH_SHORT);
                toast.show();
                MODIFICATION = true;
                finishActivity();
            }
            else {
                Toast toast = Toast.makeText(CreateFolderWithApiActivity.this,
                        "Dossier "+folder.getTitle()+" déjà existante ...",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}
