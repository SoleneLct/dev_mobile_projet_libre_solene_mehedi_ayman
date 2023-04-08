package org.o7planning.saeapplication.Activity.Other;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.o7planning.saeapplication.Activity.ImageFolderActivity;
import org.o7planning.saeapplication.Modele.Profil;
import org.o7planning.saeapplication.R;

public class AddFolderDialog implements View.OnClickListener{

    private AlertDialog alertDialog ;
    private TextView dialogTitle;
    private TextView folderNameLabel;
    private EditText folderNameInput;
    private Button cancelButton ;
    private Button createButton;

    private View dialogView;

    private Profil profil;

    public AddFolderDialog(AlertDialog.Builder builder, LayoutInflater inflater, Profil profil) {
        View dialogView = inflater.inflate(R.layout.other_add_popup_folder, null);
        builder.setView(dialogView);

        this.dialogTitle = dialogView.findViewById(R.id.dialog_title);
        this.folderNameLabel = dialogView.findViewById(R.id.folder_name_label);
        this.folderNameInput = dialogView.findViewById(R.id.folder_name_input);
        this.cancelButton = dialogView.findViewById(R.id.cancel_button);
        this.createButton = dialogView.findViewById(R.id.create_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog = builder.create();
    }

    @Override
    public void onClick(View v) {
        alertDialog.show();
    }

    private void createImageFolder(){
        if (folderNameInput.getText().toString().trim().isEmpty()) {
            folderNameInput.setError("Merci de remplir tout les champs");
            folderNameInput.requestFocus();
        }
    }
}
