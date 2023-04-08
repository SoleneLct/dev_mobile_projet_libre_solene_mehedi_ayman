package org.o7planning.saeapplication.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.o7planning.saeapplication.Activity.Other.AddFolderDialog;
import org.o7planning.saeapplication.Modele.Profil;
import org.o7planning.saeapplication.R;

public class ImageFolderActivity extends AppCompatActivity {
    public static final int RESULT_CODE = 2;

    private ImageButton addImage;
    private ImageButton addFolder;
    private ImageButton accountButton;
    private ImageButton logoutButton;
    private RecyclerView foldersRecyclerView;
    private Profil mUtilisateur;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case ProfilActivity.RESULT_CODE_ACTIVITY:
                    Boolean hello = data.getBooleanExtra(ProfilActivity.MODIFICATION_INTENT_EXTRA,false);
                    if(hello)
                        mUtilisateur = (Profil) getIntent().getSerializableExtra("userObject");
                    break;
                default:
                    Toast.makeText(this, "result code no found", Toast.LENGTH_SHORT).show();
                    ;
            }
            // Traitement des données renvoyées par l'activité appelée
            String result = data.getStringExtra("result");
            // ...
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_folder);

        this.mUtilisateur = (Profil) getIntent().getSerializableExtra("userObject");

        accountButton = findViewById(R.id.account);
        logoutButton = findViewById(R.id.logout);
        addFolder = findViewById(R.id.addFolder);
        addImage = findViewById(R.id.addImage);
        foldersRecyclerView = findViewById(R.id.foldersRecyclerView);
        addFolder.setOnClickListener(
                new AddFolderDialog(new AlertDialog.Builder(ImageFolderActivity.this),getLayoutInflater(),mUtilisateur)
        );
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilActivity = new Intent(ImageFolderActivity.this, ProfilActivity.class);
                profilActivity.putExtra("userObject" ,mUtilisateur);
                startActivityForResult(profilActivity,ProfilActivity.RESULT_CODE_ACTIVITY);
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
