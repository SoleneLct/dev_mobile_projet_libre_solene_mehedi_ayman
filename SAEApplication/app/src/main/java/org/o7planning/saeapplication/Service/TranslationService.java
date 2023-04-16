package org.o7planning.saeapplication.Service;

import android.content.Context;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;

public class TranslationService {
    private TranslateOptions translateOptions = null;
    private Translate translate = null;
    private static TranslationService _TranslationService = null;


    private TranslationService(Context context){
        this.translateOptions = TranslateOptions.newBuilder().setApiKey(DefaultValueService.keyTranslate(context)).build();
        this.translate = translateOptions.getService();
    }
    public TranslateOptions getTranslateOptions() {
        return translateOptions;
    }
    public Translate getTranslate() {
        return translate;
    }
    public static TranslationService getTranslationService(Context context){
        if(_TranslationService == null)
            _TranslationService = new TranslationService(context);
        return _TranslationService;
    }
    public static String Translation(String text ,Context context) {
        return TranslationService.
                getTranslationService(context).
                getTranslate().
                translate(text, Translate.TranslateOption.targetLanguage("fr")).
                getTranslatedText();
    }
}
