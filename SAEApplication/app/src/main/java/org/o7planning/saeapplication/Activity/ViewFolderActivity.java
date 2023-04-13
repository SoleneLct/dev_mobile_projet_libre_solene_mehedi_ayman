package org.o7planning.saeapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.o7planning.saeapplication.Activity.Other.ImageAdapter;
import org.o7planning.saeapplication.Database.DataBaseImageManager;
import org.o7planning.saeapplication.Modele.Folder;
import org.o7planning.saeapplication.Modele.Image;
import org.o7planning.saeapplication.Modele.Profil;
import org.o7planning.saeapplication.R;

import java.util.List;

public class ViewFolderActivity extends AppCompatActivity {
    public static final int RESULT_CODE = 6;
    private ImageButton addImage;
    private ImageButton accountButton;
    private ImageButton logoutButton;
    private RecyclerView imagesRecyclerView;
    private Profil mUtilisateur;
    private Folder mFolder;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Boolean hello ;
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case ViewProfilActivity.RESULT_CODE_ACTIVITY:
                    hello = data.getBooleanExtra(ViewProfilActivity.MODIFICATION_INTENT_EXTRA,false);
                    if(hello)
                        mUtilisateur = (Profil) getIntent().getSerializableExtra("userObject");
                    break;
                case StoreImageActivity.RESULT_CODE_ACTIVITY:
                    hello = data.getBooleanExtra(ViewProfilActivity.MODIFICATION_INTENT_EXTRA,false);
                    if(hello)
                        refreshView();
                    break;
                default:
                    Toast.makeText(this, "result code no found", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_folder);
        this.mUtilisateur = (Profil) getIntent().getSerializableExtra("userObject");
        this.mFolder = (Folder) getIntent().getSerializableExtra("folderObject");

        this.accountButton = findViewById(R.id.account);
        this.logoutButton = findViewById(R.id.logout);
        this.addImage = findViewById(R.id.addImage);
        this.imagesRecyclerView = findViewById(R.id.foldersRecyclerView);
        this.accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilActivity = new Intent(ViewFolderActivity.this, ViewProfilActivity.class);
                profilActivity.putExtra("userObject" ,mUtilisateur);
                startActivityForResult(profilActivity, ViewProfilActivity.RESULT_CODE_ACTIVITY);
            }
        });
        this.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilActivity = new Intent(ViewFolderActivity.this, StoreImageActivity.class);
                profilActivity.putExtra("userObject" ,mUtilisateur);
                startActivityForResult(profilActivity, StoreImageActivity.RESULT_CODE_ACTIVITY);
            }
        });
        this.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        refreshView();
    }

    public void refreshView(){
        DataBaseImageManager db = new DataBaseImageManager(ViewFolderActivity.this);
        List<Image> imageList = db.getAllImagesByUserIdAndFolder(mUtilisateur.getId(),mFolder.getId());
        List<Image> img = db.getAllImages();
        ImageAdapter adapter = new ImageAdapter(imageList, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        this.imagesRecyclerView.setAdapter(adapter);
        this.imagesRecyclerView.setLayoutManager(layoutManager);
    }
}
