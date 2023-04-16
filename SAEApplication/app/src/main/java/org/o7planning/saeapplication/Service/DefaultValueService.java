package org.o7planning.saeapplication.Service;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DefaultValueService {
    private static final String JSONIMAGE_KEY = "{\n" +
            "  \"type\": \"service_account\",\n" +
            "  \"project_id\": \"saedevmobile\",\n" +
            "  \"private_key_id\": \"d7c4374be80dd528070659342c07eebc2f69ee99\",\n" +
            "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDup9j1vX9slNhC\\nF2CflzWk7HY9qDilq80RI06lm4xRSyJYCx7BmPrqaqkDENuQFDpiaGdVI9F1D8yW\\nPAxgB1vQWU8mqX9YXzz2OzrQ5ljxR1NjkMcmup/FZamO4SXTvF6oi+cmKnMmjlus\\nfbZQPMC8+3//SMqObd0Y/JYW1YexxERAXZ8wRXBRyj8PgSdJX1B3qIYGjA0wF4qj\\nb9m6CSQpz2OMRlrsRPMcYzEQW1BmTHHyNkKX4tUT8SIXXommlcZkbvCv/GZY5oSQ\\n/jo+T9OpRCIOx9BId2q1BROYkqDu0es9XSD74Oxtogq1IiUwrdXgMORe/GNtRgj1\\nSDnsvjKNAgMBAAECggEALZ+7Z6E6OLcMz7e54lCNMhH9ecSMzck1+xqBmMHPlaP0\\nNRotH0wgXcuVs28X1mezkKgOZxWhn4wta6QfpoUJnZ9RNR4uqOWOGTGSCSkcJEYj\\nMDPXirUfQaSxgU3UBjIq83RJdH+lg1+wMV3YCmdO3mIDInnXNVrV64awJ73bAtXY\\nwx2Io5Z8OC3aCIe9lInBPE461e6JmUOvsvji/mbiUS4y1Bn+8jYX4vY9lzCgRjUt\\neLKjj6L94Z8XnntwCbwnahfO0L/weIv8WV8ZEL2d7TJ+hA1aRV3CGPQ5nxbxvacq\\nOndBWkRuh5Qkj311MMxIXactMX9q4ZqSObJ7SPbpsQKBgQD7/8BJ4VRQ+jH1ws6I\\nPx8dntX4FySGtOFHo7jdqDvSeRtRlIcMc0eg5JRiTagPwXIBhXYwKIMT3r7MXbNo\\nbG64adO0CfKBmadzETRXbdps3O07y9Nfg/ePvhLELa9Ywb18nLEk14MtArYQe5/1\\nknDgpLIO2ZTFCpUe0W2ZKHOx8QKBgQDycdy/Obg8W9k9DrqjkgqrNNKiHqNL3PE6\\nxCk57LAe+BdEYprzPCovV8R/hdS6RQs9XtFc/pu69pSJPD/hs9j8vRfpJy4kbUKD\\ncQ64kTb4XzAeTZ+hvwHpgjN5ghkuvCnyBkUo2Am7OosKWULUbO3ATAJyOS6qBtsc\\nvKXVp1FuXQKBgQCa9JHeHDPHs4GDGK39M6tDgIccDccFGjao4bDsg+V9L8EvsgA1\\nFLIH6maf6Bae0Znz+4hIZrDrR/3VFw0Z+gs/VLu8Kp23Oa+w3vRrin5UkKlEKrqo\\nMjLJbdyGS31WO5P0CzoWaHsd81vW2N3smIbWbA+deAe1/iLiF2+oTSqPUQKBgDzm\\nGPr3tq2CrMjHgF5B+ugSdq4+X+pYb0fBqlFggMTmsIF++0kJ3dbShws5RLwdR7t8\\nq75ePiPyeFEYjztUf7bMkMsCCncf8ssthPMbluFAl8ek2+o3HnXXPLLS2RyCK08e\\nABVIXvlmtTI+RakyS244KY0ji/MtkbBgOo+2XaAJAoGBAKte9tJbGe71Y7raYxI+\\nnq7Mux1SP3QAihCRz4ETUUgsSRMBgat1qaCGh4NReKaNOdn/blVquWTxfwAo+kEq\\nuqWBXcwimKbXm0HWMcfOO4q79Fk8hD7Fa+DD1YHMx98vEmAtCN8eFGDpTQZplZoT\\n5BQXszs/PZHEq3xSVV90+IOd\\n-----END PRIVATE KEY-----\\n\",\n" +
            "  \"client_email\": \"comptesae1@saedevmobile.iam.gserviceaccount.com\",\n" +
            "  \"client_id\": \"105935142916458835458\",\n" +
            "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
            "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
            "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
            "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/comptesae1%40saedevmobile.iam.gserviceaccount.com\"\n" +
            "}\n";
    private static final String TRANSLATE_KEY = "AIzaSyBOLgV8xbrL2huPNljlf4S4Sezk7lIguq8";

    public static String keyTranslate(Context context){
        return TRANSLATE_KEY;
    }

    public static FileInputStream jsonConnectionString(Context context){
        byte[] bytes = JSONIMAGE_KEY.getBytes();
        FileInputStream fis = null;
        try {
            // Créer un fichier temporaire pour stocker le texte
            File tempFile = File.createTempFile("temp", ".json");
            FileWriter writer = new FileWriter(tempFile);
            writer.write(JSONIMAGE_KEY);
            writer.close();
            fis = new FileInputStream(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fis;
    }
}
