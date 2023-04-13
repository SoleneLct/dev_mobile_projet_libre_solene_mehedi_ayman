package org.o7planning.saeapplication.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.o7planning.saeapplication.Modele.Folder;
import org.o7planning.saeapplication.Modele.Image;
import org.o7planning.saeapplication.Modele.Profil;
import org.o7planning.saeapplication.R;

public class ViewImageActivity extends AppCompatActivity {
    private ImageButton mImage;
    private TextView mImageNom;
    private TextView mImageNomInput;
    private TextView mExit;

    private Image mImageObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        this.mImageObject = (Image) getIntent().getSerializableExtra("imageObject");
        Bitmap bitmap = BitmapFactory.decodeByteArray(mImageObject.getImage(), 0, mImageObject.getImage().length);

        mImage = findViewById(R.id.image);
        mImage.setImageBitmap(bitmap);

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profilActivity = new Intent(ViewImageActivity.this, ViewImageActivity.ImageActivity.class);
                profilActivity.putExtra("imageObject" ,mImageObject);
                startActivityForResult(profilActivity, ViewProfilActivity.RESULT_CODE_ACTIVITY);
            }
        });

        mImageNom = findViewById(R.id.image_nom);

        mImageNomInput = findViewById(R.id.image_nom_input);
        mImageNomInput.setText(mImageObject.getNom());

        mExit = findViewById(R.id.exit);
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static class ImageActivity extends AppCompatActivity {
        private Image mImageObject;
        private ImageButton mImage;
        private TextView mExit;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_image);
            this.mImageObject = (Image) getIntent().getSerializableExtra("imageObject");
            Bitmap bitmap = BitmapFactory.decodeByteArray(mImageObject.getImage(), 0, mImageObject.getImage().length);

            mImage = findViewById(R.id.image);
            mImage.setImageBitmap(bitmap);
            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            mExit = findViewById(R.id.exit);
            mExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}