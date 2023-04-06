package org.o7planning.saeapplication.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoginService {
    public static String keyTranslate(){
        InputStream inputStream = LoginService.class.getResourceAsStream("/LoginApiJson/keyTranslate.json");
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
        InputStream inputStream = LoginService.class.getResourceAsStream("/LoginApiJson/saedevmobile.json");
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
