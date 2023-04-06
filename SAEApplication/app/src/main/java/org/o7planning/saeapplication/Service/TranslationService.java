package org.o7planning.saeapplication.Service;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;

public class TranslationService {
    private TranslateOptions translateOptions = null;
    private Translate translate = null;
    private static TranslationService _TranslationService = null;


    private TranslationService(){
        this.translateOptions = TranslateOptions.newBuilder().setApiKey(LoginService.keyTranslate()).build();
        this.translate = translateOptions.getService();
    }
    public TranslateOptions getTranslateOptions() {
        return translateOptions;
    }
    public Translate getTranslate() {
        return translate;
    }
    public static TranslationService getTranslationService(){
        if(_TranslationService == null)
            _TranslationService = new TranslationService();
        return _TranslationService;
    }
    public static String Translation(String text) {
        return TranslationService.
                getTranslationService().
                getTranslate().
                translate(text, Translate.TranslateOption.targetLanguage("fr")).
                getTranslatedText();
    }
}
