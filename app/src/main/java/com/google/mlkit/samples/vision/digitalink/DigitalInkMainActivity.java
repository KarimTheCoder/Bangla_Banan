package com.google.mlkit.samples.vision.digitalink;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.mlkit.samples.vision.digitalink.StrokeManager.DownloadedModelsChangedListener;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier;
import java.util.Locale;
import java.util.Set;

import model.Word;

/** Main activity which creates a StrokeManager and connects it to the DrawingView. */
public class DigitalInkMainActivity extends AppCompatActivity
    implements View.OnClickListener,DownloadedModelsChangedListener, StrokeManager.StatusChangedListener {
  public static final String BANGLA_LANG_CODE = "bn";
  private Button match;
  private ImageButton next;
  private MainViewModel mainViewModel;
  private TextView currentWordTextView;
  private  DrawingView drawingView;
    @VisibleForTesting final StrokeManager strokeManager = new StrokeManager();
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_digital_ink_main);

    mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);


    mainViewModel.initWords();


    match = findViewById(R.id.match_button);
    next = findViewById(R.id.next_button);
    next.setOnClickListener(this);
    match.setOnClickListener(this);


    currentWordTextView = findViewById(R.id.current_word_textview);
    drawingView = findViewById(R.id.drawing_view);
    StatusTextView statusTextView = findViewById(R.id.status_text_view);
    drawingView.setStrokeManager(strokeManager);
    statusTextView.setStrokeManager(strokeManager);

    strokeManager.setStatusChangedListener(statusTextView);
    strokeManager.setStatusChangedListener(this);
    strokeManager.setContentChangedListener(drawingView);
    strokeManager.setDownloadedModelsChangedListener(this);
    strokeManager.setClearCurrentInkAfterRecognition(true);
    strokeManager.setTriggerRecognitionAfterInput(false);

    strokeManager.refreshDownloadedModelsStatus();
    strokeManager.setActiveModel(BANGLA_LANG_CODE);
    strokeManager.download();
    strokeManager.reset();

    mainViewModel.currentWord.observe(this, new Observer<Word>() {
      @Override
      public void onChanged(Word word) {

        currentWordTextView.setText(word.getWord());
      }
    });
    mainViewModel.startSpellTest();

  }

  public void recognizeClick(View v) {


  strokeManager.recognize();
  }

  public void clearClick(View v) {
    strokeManager.reset();
    drawingView.clear();
  }

  @Override
  public void onDownloadedModelsChanged(Set<String> downloadedLanguageTags) {

  }


  @Override
  public void onStatusChanged() {

    mainViewModel.setWrittenWord(strokeManager.getStatus());
    Toast.makeText(getApplicationContext(), mainViewModel.getWrittenWord(), Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onClick(View view) {

    if (view == match){

        String result = mainViewModel.matchWord();
        if(result.equalsIgnoreCase("Strings are equal.")){
            strokeManager.reset();
            drawingView.clear();
        }
        Toast.makeText(this,result,Toast.LENGTH_SHORT).show();

    }

    if( view == next){

      mainViewModel.next();

    }

  }
}
