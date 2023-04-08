package org.o7planning.saeapplication.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.o7planning.saeapplication.R;

public class ImageFolderActivity extends AppCompatActivity {

    private ImageButton accountButton;
    private ImageButton logoutButton;
    private RecyclerView foldersRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_folder);

        accountButton = findViewById(R.id.account);
        logoutButton = findViewById(R.id.logout);
        foldersRecyclerView = findViewById(R.id.foldersRecyclerView);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
