package org.o7planning.saeapplication.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.o7planning.saeapplication.Manifest;
import org.o7planning.saeapplication.R;

import java.util.Random;

public class NumTelActivity extends AppCompatActivity {

    private View numTelLayout;
    private View codeSMSLayout;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_tel);

        numTelLayout = findViewById(R.id.text_num_tel);
        codeSMSLayout = findViewById(R.id.layout_code_sms);
        codeSMSLayout.setVisibility(View.GONE);

        // Récupération de la référence du bouton d'envoi du numéro de téléphone
        Button btnEnvoi = findViewById(R.id.button_valider);

        // Ajout d'un listener sur le bouton d'envoi
        btnEnvoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupération du numéro de téléphone entré par l'utilisateur
                EditText edtNumTel = findViewById(R.id.text_num_tel);
                String numTel = edtNumTel.getText().toString();

                // Vérification du numéro de téléphone
                if (isValidPhoneNumber(numTel)) {
                    // Envoi du numéro de téléphone par SMS
                    sendSMS(numTel);

                    // Affichage du layout du code SMS
                    numTelLayout.setVisibility(View.GONE);
                    codeSMSLayout.setVisibility(View.VISIBLE);
                } else {
                    // Affichage d'un message d'erreur pour le numéro de téléphone invalide
                    Toast.makeText(NumTelActivity.this, "Numéro de téléphone invalide", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Méthode pour envoyer un SMS
    private void sendSMS(String numTel) {
        Random rand = new Random();
        int code = rand.nextInt(900000) + 100000; // génère un nombre entre 100000 et 999999
        StringBuilder sb = new StringBuilder();
        sb.append(code);
        String verificationCode = sb.toString();

        String message = "Votre code de vérification est : " + verificationCode;

        // Vérification de la permission d'envoi de SMS
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Demande de la permission d'envoi de SMS si elle n'a pas été accordée
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            // Envoi du SMS
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(numTel, null, message, null, null);
        }
    }



    // Méthode pour vérifier si un numéro de téléphone est valide
    private boolean isValidPhoneNumber(String numTel) {
        if (TextUtils.isEmpty(numTel) || numTel.length() != 10) {
            return false;
        }

        String prefix = numTel.substring(0, 2);
        if (!prefix.equals("06") && !prefix.equals("07")) {
            return false;
        }

        for (int i = 0; i < numTel.length(); i++) {
            if (!Character.isDigit(numTel.charAt(i))) {
                return false;
            }
        }

        return true;
    }


    private boolean isVerificationCodeValid(String enteredCode, String sentCode) {
        return enteredCode.equals(sentCode);
    }
}
