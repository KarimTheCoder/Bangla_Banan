package com.google.mlkit.samples.vision.digitalink.kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.VisibleForTesting
import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSortedSet
import com.google.mlkit.samples.vision.digitalink.R
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import java.util.Locale

/**
 * Main activity that creates a StrokeManager and connects it to the DrawingView.
 */
class DigitalInkMainActivity : AppCompatActivity(), StrokeManager.DownloadedModelsChangedListener {

  @JvmField
  @VisibleForTesting
  val strokeManager = StrokeManager()

  private lateinit var languageAdapter: ArrayAdapter<ModelLanguageContainer>

  /**
   * Called when the activity is first created.
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_digital_ink_main_kotlin)

    // Initialize UI elements
    val languageSpinner = findViewById<Spinner>(R.id.languages_spinner)
    val drawingView = findViewById<DrawingView>(R.id.drawing_view)
    val statusTextView = findViewById<StatusTextView>(R.id.status_text_view)

    // Set StrokeManager to DrawingView and StatusTextView
    drawingView.setStrokeManager(strokeManager)
    statusTextView.setStrokeManager(strokeManager)

    // Set StrokeManager listeners
    strokeManager.setStatusChangedListener(statusTextView)
    strokeManager.setContentChangedListener(drawingView)
    strokeManager.setDownloadedModelsChangedListener(this)

    // Configure recognition behavior
    strokeManager.setClearCurrentInkAfterRecognition(true)
    strokeManager.setTriggerRecognitionAfterInput(false)

    // Set up the language adapter for the spinner
    languageAdapter = populateLanguageAdapter()
    languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    languageSpinner.adapter = languageAdapter

    // Refresh downloaded models status
    strokeManager.refreshDownloadedModelsStatus()

    // Set up language selection listener
    languageSpinner.onItemSelectedListener = object : OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val languageCode = (parent.adapter.getItem(position) as ModelLanguageContainer).languageTag ?: return
        Log.i(TAG, "Selected language: $languageCode")
        strokeManager.setActiveModel(languageCode)
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.i(TAG, "No language selected")
      }
    }

    // Reset the StrokeManager to its initial state
    strokeManager.reset()
  }

  // Button click handlers
  fun downloadClick(v: View?) {
    strokeManager.download()
  }

  fun recognizeClick(v: View?) {
    strokeManager.recognize()
  }

  fun clearClick(v: View?) {
    strokeManager.reset()
    val drawingView = findViewById<DrawingView>(R.id.drawing_view)
    drawingView.clear()
  }

  fun deleteClick(v: View?) {
    strokeManager.deleteActiveModel()
  }

  /**
   * Container class for model language, used in the Spinner for language selection.
   */
  private class ModelLanguageContainer private constructor(private val label: String, val languageTag: String?) : Comparable<ModelLanguageContainer> {

    var downloaded: Boolean = false

    override fun toString(): String {
      return when {
        languageTag == null -> label
        downloaded -> "   [D] $label"
        else -> "   $label"
      }
    }

    override fun compareTo(other: ModelLanguageContainer): Int {
      return label.compareTo(other.label)
    }

    companion object {
      /** Creates a model container with both label and language tag. */
      fun createModelContainer(label: String, languageTag: String?): ModelLanguageContainer {
        return ModelLanguageContainer(label, languageTag)
      }

      /** Creates a model container with label only, without a language tag. */
      fun createLabelOnly(label: String): ModelLanguageContainer {
        return ModelLanguageContainer(label, null)
      }
    }
  }

  /**
   * Populates the language adapter with available models and categories.
   */
  private fun populateLanguageAdapter(): ArrayAdapter<ModelLanguageContainer> {
    val languageAdapter = ArrayAdapter<ModelLanguageContainer>(this, android.R.layout.simple_spinner_item)

    // Add default options
    languageAdapter.add(ModelLanguageContainer.createLabelOnly("Select language"))
    languageAdapter.add(ModelLanguageContainer.createLabelOnly("Non-text Models"))

    // Add non-text models
    for (languageTag in NON_TEXT_MODELS.keys) {
      languageAdapter.add(ModelLanguageContainer.createModelContainer(NON_TEXT_MODELS[languageTag]!!, languageTag))
    }

    // Add text models
    languageAdapter.add(ModelLanguageContainer.createLabelOnly("Text Models"))
    val textModels = ImmutableSortedSet.naturalOrder<ModelLanguageContainer>()
    for (modelIdentifier in DigitalInkRecognitionModelIdentifier.allModelIdentifiers()) {
      if (NON_TEXT_MODELS.containsKey(modelIdentifier.languageTag)) continue
      if (modelIdentifier.languageTag.endsWith(Companion.GESTURE_EXTENSION)) continue
      textModels.add(buildModelContainer(modelIdentifier, "Script"))
    }
    languageAdapter.addAll(textModels.build())

    // Add gesture models
    languageAdapter.add(ModelLanguageContainer.createLabelOnly("Gesture Models"))
    val gestureModels = ImmutableSortedSet.naturalOrder<ModelLanguageContainer>()
    for (modelIdentifier in DigitalInkRecognitionModelIdentifier.allModelIdentifiers()) {
      if (!modelIdentifier.languageTag.endsWith(Companion.GESTURE_EXTENSION)) continue
      gestureModels.add(buildModelContainer(modelIdentifier, "Script gesture classifier"))
    }
    languageAdapter.addAll(gestureModels.build())

    return languageAdapter
  }

  /**
   * Builds a model container with the label for the Spinner.
   */
  private fun buildModelContainer(modelIdentifier: DigitalInkRecognitionModelIdentifier, labelSuffix: String): ModelLanguageContainer {
    val label = StringBuilder()
    label.append(Locale(modelIdentifier.languageSubtag).displayName)

    if (modelIdentifier.regionSubtag != null) {
      label.append(" (").append(modelIdentifier.regionSubtag).append(")")
    }
    if (modelIdentifier.scriptSubtag != null) {
      label.append(", ").append(modelIdentifier.scriptSubtag).append(" ").append(labelSuffix)
    }

    return ModelLanguageContainer.createModelContainer(label.toString(), modelIdentifier.languageTag)
  }

  /**
   * Updates the Spinner with downloaded model information.
   */
  override fun onDownloadedModelsChanged(downloadedLanguageTags: Set<String>) {
    for (i in 0 until languageAdapter.count) {
      val container = languageAdapter.getItem(i)!!
      container.downloaded = downloadedLanguageTags.contains(container.languageTag)
    }
    languageAdapter.notifyDataSetChanged()
  }

  companion object {
    private const val TAG = "MLKDI.Activity"

    // Non-text models available
    private val NON_TEXT_MODELS = ImmutableMap.of(
      "zxx-Zsym-x-autodraw", "Autodraw",
      "zxx-Zsye-x-emoji", "Emoji",
      "zxx-Zsym-x-shapes", "Shapes"
    )

    private const val GESTURE_EXTENSION = "-x-gesture"
  }
}