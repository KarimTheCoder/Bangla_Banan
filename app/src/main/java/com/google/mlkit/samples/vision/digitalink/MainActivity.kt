package com.google.mlkit.samples.vision.digitalink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.google.mlkit.samples.vision.digitalink.kotlin.componenets.MyDrawerLayout
import com.google.mlkit.samples.vision.digitalink.kotlin.componenets.MyScaffold
import com.google.mlkit.samples.vision.digitalink.ui.theme.MLKitDigitalInkRecognitionDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MLKitDigitalInkRecognitionDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    MyDrawerLayout { drawerState, scope ->
                        MyScaffold(drawerState, scope)
                    }

                }
            }
        }
    }
}

