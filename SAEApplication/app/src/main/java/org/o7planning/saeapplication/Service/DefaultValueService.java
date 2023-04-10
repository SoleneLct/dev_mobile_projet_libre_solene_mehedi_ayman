package org.o7planning.saeapplication.Service;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.o7planning.saeapplication.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DefaultValueService {
    public static String keyTranslate(){
        InputStream inputStream = DefaultValueService.class.getResourceAsStream("/LoginApiJson/keyTranslate.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }
        } catch (IOException e) {
            return "";
        }
        return stringBuilder.toString();
    }
    public static String jsonConnectionString(){
        InputStream inputStream = DefaultValueService.class.getResourceAsStream("/LoginApiJson/saedevmobile.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(System.lineSeparator());
            }
        } catch (IOException e) {
            return "";
        }
        return stringBuilder.toString();
    }

}
