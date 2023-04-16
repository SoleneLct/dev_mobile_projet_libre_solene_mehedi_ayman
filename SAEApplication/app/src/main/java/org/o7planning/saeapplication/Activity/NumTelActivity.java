package org.o7planning.saeapplication.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.o7planning.saeapplication.Database.DataBaseProfilsManager;
import org.o7planning.saeapplication.Modele.Profil;
import org.o7planning.saeapplication.R;

import java.util.Random;

public class NumTelActivity extends AppCompatActivity {

    public static final int RESULT_CODE_ACTIVITY = 7;
    public static final String MODIFICATION_INTENT_EXTRA = "MODIFICATION_INTENT_EXTRA";
    private View numTelLayout;
    private EditText textCodeSms;
    private LinearLayout codeSMSLayout,numSMSLayout;

    private Button buttonValiderCodeSms;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    private String value ;
    private TextView quitte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_tel);

        Profil utilisateur = (Profil) getIntent().getSerializableExtra("userObject");

        numTelLayout = findViewById(R.id.text_num_tel);
        codeSMSLayout = findViewById(R.id.layout_code_sms);

        numSMSLayout = findViewById(R.id.layout_num_sms);
        quitte = findViewById(R.id.exit);
        quitte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(MODIFICATION_INTENT_EXTRA,false);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        textCodeSms = findViewById(R.id.text_code_sms);
        buttonValiderCodeSms = findViewById(R.id.button_valider_code_sms);
        buttonValiderCodeSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = textCodeSms.getText().toString().trim();
                if (type.equals(value)){
                    DataBaseProfilsManager db = new DataBaseProfilsManager(NumTelActivity.this);
                    db.addProfils(utilisateur);

                    Intent intent = new Intent();
                    intent.putExtra(MODIFICATION_INTENT_EXTRA,true);
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(NumTelActivity.this, "Code invalide", Toast.LENGTH_SHORT).show();
                    numSMSLayout.setVisibility(View.VISIBLE);
                    codeSMSLayout.setVisibility(View.GONE);
                    value="";
                }
            }
        });
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
                    value = sendSMS(numTel);
                    if(!value.isEmpty()){
                        // Affichage du layout du code SMS
                        numSMSLayout.setVisibility(View.GONE);
                        codeSMSLayout.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(NumTelActivity.this, "Veuillez accordée cette permission d'envoi de SMS)", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(NumTelActivity.this, new String[] {android.Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                    }
                } else {
                    // Affichage d'un message d'erreur pour le numéro de téléphone invalide
                    Toast.makeText(NumTelActivity.this, "Numéro de téléphone invalide", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Méthode pour envoyer un SMS
    private String sendSMS(String numTel) {
        Random rand = new Random();
        int code = rand.nextInt(900000) + 100000; // génère un nombre entre 100000 et 999999
        StringBuilder sb = new StringBuilder();
        sb.append(code);
        String verificationCode = sb.toString();

        String message = "Votre code de vérification est : " + verificationCode;

        // Vérification de la permission d'envoi de SMS
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(numTel, null, message, null, null);
            return verificationCode;
        }
        // Envoi du SMS
        ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        return "";
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
