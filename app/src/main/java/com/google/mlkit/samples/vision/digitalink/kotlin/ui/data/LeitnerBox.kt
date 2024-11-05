package com.google.mlkit.samples.vision.digitalink.kotlin.ui.data

data class LeitnerBox(
    val level: Int,
    val intervalDays: Int
)


object LeitnerBoxConfig {
    val boxes: List<LeitnerBox> = listOf(
        LeitnerBox(level = 0, intervalDays = 1),  // Box 0 reviewed daily
        LeitnerBox(level = 1, intervalDays = 3),  // Box 1 reviewed every 3 days
        LeitnerBox(level = 2, intervalDays = 7),  // Box 2 reviewed every 7 days
        LeitnerBox(level = 3, intervalDays = 14), // Box 3 reviewed every 2 weeks
        LeitnerBox(level = 4, intervalDays = 30)  // Box 4 reviewed monthly
    )
}