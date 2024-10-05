package com.google.mlkit.samples.vision.digitalink;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.mlkit.samples.vision.digitalink.StrokeManager.DownloadedModelsChangedListener;
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier;
import java.util.Locale;
import java.util.Set;

/** Main activity which creates a StrokeManager and connects it to the DrawingView. */
public class DigitalInkMainActivity extends AppCompatActivity
    implements DownloadedModelsChangedListener {
  public static final String BANGLA_LANG_CODE = "bn";
  private static final String TAG = "MLKDI.Activity";
  private static final String GESTURE_EXTENSION = "-x-gesture";
  private static final ImmutableMap<String, String> NON_TEXT_MODELS =
      ImmutableMap.of(
          "zxx-Zsym-x-autodraw",
          "Autodraw",
          "zxx-Zsye-x-emoji",
          "Emoji",
          "zxx-Zsym-x-shapes",
          "Shapes");
  @VisibleForTesting final StrokeManager strokeManager = new StrokeManager();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_digital_ink_main);


    DrawingView drawingView = findViewById(R.id.drawing_view);
    StatusTextView statusTextView = findViewById(R.id.status_text_view);
    drawingView.setStrokeManager(strokeManager);
    statusTextView.setStrokeManager(strokeManager);

    strokeManager.setStatusChangedListener(statusTextView);
    strokeManager.setContentChangedListener(drawingView);
    strokeManager.setDownloadedModelsChangedListener(this);
    strokeManager.setClearCurrentInkAfterRecognition(true);
    strokeManager.setTriggerRecognitionAfterInput(false);

    strokeManager.refreshDownloadedModelsStatus();
    strokeManager.setActiveModel(BANGLA_LANG_CODE);
    strokeManager.download();

    strokeManager.reset();
  }

  public void downloadClick(View v) {
    strokeManager.download();
  }

  public void recognizeClick(View v) {
    strokeManager.recognize();
  }

  public void clearClick(View v) {
    strokeManager.reset();
    DrawingView drawingView = findViewById(R.id.drawing_view);
    drawingView.clear();
  }

  public void deleteClick(View v) {
    strokeManager.deleteActiveModel();
  }

  @Override
  public void onDownloadedModelsChanged(Set<String> downloadedLanguageTags) {

  }

  private static class ModelLanguageContainer implements Comparable<ModelLanguageContainer> {
    private final String label;
    @Nullable private final String languageTag;
    private boolean downloaded;

    private ModelLanguageContainer(String label, @Nullable String languageTag) {
      this.label = label;
      this.languageTag = languageTag;
    }

    /**
     * Populates and returns a real model identifier, with label, language tag and downloaded
     * status.
     */
    public static ModelLanguageContainer createModelContainer(String label, String languageTag) {
      // Offset the actual language labels for better readability
      return new ModelLanguageContainer(label, languageTag);
    }

    /** Populates and returns a label only, without a language tag. */
    public static ModelLanguageContainer createLabelOnly(String label) {
      return new ModelLanguageContainer(label, null);
    }

    public String getLanguageTag() {
      return languageTag;
    }

    public void setDownloaded(boolean downloaded) {
      this.downloaded = downloaded;
    }

    @NonNull
    @Override
    public String toString() {
      if (languageTag == null) {
        return label;
      } else if (downloaded) {
        return "   [D] " + label;
      } else {
        return "   " + label;
      }
    }

    @Override
    public int compareTo(ModelLanguageContainer o) {
      return label.compareTo(o.label);
    }
  }



  private ArrayAdapter<ModelLanguageContainer> populateLanguageAdapter() {
    ArrayAdapter<ModelLanguageContainer> languageAdapter =
        new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
    languageAdapter.add(ModelLanguageContainer.createLabelOnly("Select language"));
    languageAdapter.add(ModelLanguageContainer.createLabelOnly("Non-text Models"));

    // Manually add non-text models first
    for (String languageTag : NON_TEXT_MODELS.keySet()) {
      languageAdapter.add(
          ModelLanguageContainer.createModelContainer(
              NON_TEXT_MODELS.get(languageTag), languageTag));
    }
    languageAdapter.add(ModelLanguageContainer.createLabelOnly("Text Models"));

    ImmutableSortedSet.Builder<ModelLanguageContainer> textModels =
        ImmutableSortedSet.naturalOrder();
    for (DigitalInkRecognitionModelIdentifier modelIdentifier :
        DigitalInkRecognitionModelIdentifier.allModelIdentifiers()) {
      if (NON_TEXT_MODELS.containsKey(modelIdentifier.getLanguageTag())) {
        continue;
      }
      if (modelIdentifier.getLanguageTag().endsWith(GESTURE_EXTENSION)) {
        continue;
      }

      textModels.add(buildModelContainer(modelIdentifier, "Script"));
    }
    languageAdapter.addAll(textModels.build());

    languageAdapter.add(ModelLanguageContainer.createLabelOnly("Gesture Models"));

    ImmutableSortedSet.Builder<ModelLanguageContainer> gestureModels =
        ImmutableSortedSet.naturalOrder();
    for (DigitalInkRecognitionModelIdentifier modelIdentifier :
        DigitalInkRecognitionModelIdentifier.allModelIdentifiers()) {
      if (!modelIdentifier.getLanguageTag().endsWith(GESTURE_EXTENSION)) {

        continue;

      }

      gestureModels.add(buildModelContainer(modelIdentifier, "Script gesture classifier"));
    }
    languageAdapter.addAll(gestureModels.build());
    return languageAdapter;
  }

  private static ModelLanguageContainer buildModelContainer(
      DigitalInkRecognitionModelIdentifier modelIdentifier, String labelSuffix) {
    StringBuilder label = new StringBuilder();
    label.append(new Locale(modelIdentifier.getLanguageSubtag()).getDisplayName());
    if (modelIdentifier.getRegionSubtag() != null) {
      label.append(" (").append(modelIdentifier.getRegionSubtag()).append(")");
    }

    if (modelIdentifier.getScriptSubtag() != null) {
      label.append(", ").append(modelIdentifier.getScriptSubtag()).append(" ").append(labelSuffix);
    }
    return ModelLanguageContainer.createModelContainer(
        label.toString(), modelIdentifier.getLanguageTag());
  }
}
