package com.google.mlkit.samples.vision.digitalink.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TTSHelper(context: Context) {

    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                val googleTtsEngine = "com.google.android.tts"
                try {
                    tts?.setEngineByPackageName(googleTtsEngine)
                    Log.d("TTS", "Using Google TTS engine.")
                } catch (e: Exception) {
                    Log.e("TTS", "Google TTS not found, using default.")
                }

                val langStatus = tts?.isLanguageAvailable(Locale("bn"))
                if (langStatus == TextToSpeech.LANG_AVAILABLE) {
                    tts?.language = Locale("bn")
                } else {
                    tts?.language = Locale("en")
                }
            } else {
                Log.e("TTS", "TTS init failed")
            }
        })
    }

    fun speak(text: String?) {
        text?.let {
            tts?.speak(it, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun shutdown() {
        tts?.shutdown()
    }
}