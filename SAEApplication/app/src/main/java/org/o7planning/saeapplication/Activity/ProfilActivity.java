package org.o7planning.saeapplication.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import org.o7planning.saeapplication.Database.DataBaseProfilsManager;
import org.o7planning.saeapplication.Modele.Profil;
import org.o7planning.saeapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ProfilActivity extends AppCompatActivity{

    private static final int PROFILS_CREATION_ACTIVITY_REQUEST_CODE = 42;
    public static final String BUNDLE_CREATION_ANNULATION = "BUNDLE_CREATION_ANNULATION";

    private ListView listView;
    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;
    private static final int MY_REQUEST_CODE = 1000;
    private final List<Profil> noteList = new ArrayList<Profil>();
    private ArrayAdapter<Profil> listViewAdapter;

    private Button buttonImage;
    private Button buttonSave;
    private Button buttonCancel;

    private EditText TextNom;
    private EditText TextPrenom;
    private EditText TextAdresse;
    private VideoView videoView;
    private ImageView imageView;
    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_profils);

        this.buttonImage = (Button) this.findViewById(R.id.button_image);
        this.imageView = (ImageView) this.findViewById(R.id.imageView);
        this.buttonSave = (Button) this.findViewById(R.id.button_save);
        this.buttonCancel = (Button) this.findViewById(R.id.button_cancel);
        this.TextNom = (EditText) this.findViewById(R.id.editTextTextPersonName);
        this.TextPrenom = (EditText) this.findViewById(R.id.editTextPrenom);
        this.TextAdresse = (EditText) this.findViewById(R.id.editTextTextAdresse);

        this.buttonImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        this.buttonSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        this.buttonCancel.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(BUNDLE_CREATION_ANNULATION, "annulation");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    private void save(){
        DataBaseProfilsManager db = new DataBaseProfilsManager(this);
        String nom = TextNom.getText().toString();
        String prenom = TextPrenom.getText().toString();
        String mdp = TextAdresse.getText().toString();
        Profil profil = new Profil(nom,prenom,mdp);
        db.addProfils(profil);

//        try (FileOutputStream out = new FileOutputStream(nom+prenom)) {
//            Bitmap bm=((BitmapDrawable)imageView.getDrawable()).getBitmap();
//            this.imageBP = bm;
//            this.imageBP.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
//            // PNG is a lossless format, the compression factor (100) is ignored
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // Need to put image in bd here


        Intent intent = new Intent();
        intent.putExtra(BUNDLE_CREATION_ANNULATION, "creation");
        setResult(RESULT_OK, intent);
        finish();
    }
    private void captureImage() {
        // Create an implicit intent, for image capture.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Start camera and wait for the results.
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }


    // When you have the request results
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case REQUEST_ID_READ_WRITE_PERMISSION: {
                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (read/write).
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();

                }
                // Cancelled or denied.
                else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
    private Bitmap imageBP;
    // When results returned
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                this.imageBP = (Bitmap) data.getExtras().get("data");

                this.imageView.setImageBitmap(imageBP);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
