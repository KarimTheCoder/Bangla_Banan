package com.google.mlkit.samples.vision.digitalink.kotlin.ui.lesson

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.draw.StrokeManager
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.navigation.AppNavigation
import com.google.mlkit.samples.vision.digitalink.ui.theme.MLKitDigitalInkRecognitionDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MLKitDigitalInkRecognitionDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()
                ) { innerPadding ->


                    AppNavigation()




                }
            }
        }
    }
}

