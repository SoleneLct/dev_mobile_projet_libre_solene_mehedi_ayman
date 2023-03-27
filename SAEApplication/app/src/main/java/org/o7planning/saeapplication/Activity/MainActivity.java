package org.o7planning.saeapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.o7planning.saeapplication.R;

public class MainActivity extends AppCompatActivity {
    private Button buttonCreation;
    private Button buttonHistorique;

    private static final int CREATION_PROFILS_REQUEST_CODE = 42;
    private static final int HISTORIQUE_REQUEST_CODE = 41;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
