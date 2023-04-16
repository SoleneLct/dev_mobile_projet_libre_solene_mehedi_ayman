package org.o7planning.saeapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.o7planning.saeapplication.Activity.Other.CreateFolderDialog;
import org.o7planning.saeapplication.Database.DataBaseFolderImageManager;
import org.o7planning.saeapplication.Modele.Folder;
import org.o7planning.saeapplication.Activity.Other.FolderAdapter;
import org.o7planning.saeapplication.Modele.Profil;
import org.o7planning.saeapplication.R;

import java.util.List;

public class ViewAllFolderActivity extends AppCompatActivity{
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
                    if(hello){
                        refreshView();
                    }
                    break;
                default:
                    Toast.makeText(this, "result code no found", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_folder);
        this.mUtilisateur = (Profil) getIntent().getSerializableExtra("userObject");
        this.accountButton = findViewById(R.id.account);
        this.logoutButton = findViewById(R.id.logout);
        this.addFolder = findViewById(R.id.addFolder);
        this.addImage = findViewById(R.id.addImage);
        this.foldersRecyclerView = findViewById(R.id.foldersRecyclerView);
        this.addFolder.setOnClickListener(
                new CreateFolderDialog(this,getLayoutInflater(),mUtilisateur)
        );
        this.accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilActivity = new Intent(ViewAllFolderActivity.this, ViewProfilActivity.class);
                profilActivity.putExtra("userObject" ,mUtilisateur);
                startActivityForResult(profilActivity, ViewProfilActivity.RESULT_CODE_ACTIVITY);
            }
        });
        this.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profilActivity = new Intent(ViewAllFolderActivity.this, StoreImageActivity.class);
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
        DataBaseFolderImageManager db = new DataBaseFolderImageManager(ViewAllFolderActivity.this);
        List<Folder> foldersList = db.getAllFolderImagesByUserId(mUtilisateur.getId());
        FolderAdapter adapter = new FolderAdapter(foldersList, ViewAllFolderActivity.this,mUtilisateur);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        this.foldersRecyclerView.setAdapter(adapter);
        this.foldersRecyclerView.setLayoutManager(layoutManager);
    }
}
