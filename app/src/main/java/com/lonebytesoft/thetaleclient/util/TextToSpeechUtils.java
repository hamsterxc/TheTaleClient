package com.lonebytesoft.thetaleclient.util;

import android.content.Context;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Hamster
 * @since 30.11.2014
 */
public class TextToSpeechUtils {

    private static final long PAUSE = 500;

    private static TextToSpeech textToSpeech;

    private static boolean isInitialized = false;
    private static List<String> queue = new ArrayList<>();

    public static void init(final Context context) {
        if(!isInitialized || (textToSpeech == null)) {
            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    final int initStatus = status;
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            if (initStatus == TextToSpeech.SUCCESS) {
                                if (textToSpeech.setLanguage(new Locale("ru")) >= TextToSpeech.LANG_AVAILABLE) {
                                    isInitialized = true;
                                    for (final String text : queue) {
                                        speakText(text);
                                    }
                                    queue.clear();
                                }
                            }
                            return null;
                        }
                    }.execute();
                }
            });
        }
    }

    public static void speak(final String text) {
        if(isInitialized) {
            speakText(text);
        } else {
            queue.add(text);
        }
    }

    private static void speakText(final String text) {
        if(textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
            textToSpeech.playSilence(PAUSE, TextToSpeech.QUEUE_ADD, null);
        }
    }

    public static void pause() {
        if(textToSpeech != null) {
            textToSpeech.stop();
        }
        queue.clear();
    }

    public static void destroy() {
        if(textToSpeech != null) {
            textToSpeech.shutdown();
        }
        queue.clear();
        isInitialized = false;
    }

}
