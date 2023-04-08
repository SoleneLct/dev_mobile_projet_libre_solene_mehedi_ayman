package org.o7planning.saeapplication.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import org.o7planning.saeapplication.Activity.Other.AddFolderDialog;
import org.o7planning.saeapplication.R;

public class ImageFolderActivity extends AppCompatActivity {

    private ImageButton addImage;
    private ImageButton addFolder;
    private ImageButton accountButton;
    private ImageButton logoutButton;
    private RecyclerView foldersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_folder);

        accountButton = findViewById(R.id.account);
        logoutButton = findViewById(R.id.logout);
        addFolder = findViewById(R.id.addFolder);
        addImage = findViewById(R.id.addImage);
        foldersRecyclerView = findViewById(R.id.foldersRecyclerView);
        addFolder.setOnClickListener(
                new AddFolderDialog(new AlertDialog.Builder(ImageFolderActivity.this),getLayoutInflater())
        );
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
