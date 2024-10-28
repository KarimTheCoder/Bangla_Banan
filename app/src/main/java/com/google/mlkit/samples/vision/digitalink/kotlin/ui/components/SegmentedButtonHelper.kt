package com.google.mlkit.samples.vision.digitalink.kotlin.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.edit.EditViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.complete.SessionCompleteViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.lesson.MainViewModel


@Composable
fun SegmentedButton() {

    val myViewModel: MainViewModel = viewModel()

    val optionItems = listOf(
        OptionItem(
            name = "Practice",
            icon = Icons.Filled.PlayArrow,
            onClick = {
                Log.d("SegmentedButton", "Practice clicked!")
                myViewModel.toggleIsPractice(true)
            }
        ),
        OptionItem(
            name = "Edit",
            icon = Icons.Filled.Edit,
            onClick = {
                Log.d("SegmentedButton", "Edit clicked!")
                myViewModel.toggleIsPractice(false)
            }
        )
    )

    SetSegmentedButtons(optionItems)
}

@Composable
fun SessionCompletedSegmentedButton() {

    val viewModel: SessionCompleteViewModel = viewModel()


    val optionItems = listOf(
        OptionItem(
            name = "Result",
            icon = Icons.Filled.Done,
            onClick = {
                Log.d("SegmentedButton", "Result clicked!")
                viewModel.toggleVisibility(false)
            }
        ),
        OptionItem(
            name = "Lesson",
            icon = Icons.Filled.Menu,
            onClick = {
                Log.d("SegmentedButton", "Lesson clicked!")
                viewModel.toggleVisibility(true)
            }
        )
    )

    SetSegmentedButtons(optionItems)
}

@Composable
fun EditSegmentedButton() {

    val myViewModel: EditViewModel = viewModel()

    val optionItems = listOf(
        OptionItem(
            name = "Type",
            icon = Icons.Filled.Edit,
            onClick = {
                Log.d("SegmentedButton", "Type")
                myViewModel.toggleExpansion(false)
            }
        ),
        OptionItem(
            name = "Import",
            icon = Icons.Default.Add,
            onClick = {
                Log.d("SegmentedButton", "Import clicked!")
                myViewModel.toggleExpansion(true)
            }
        )
    )

    SetSegmentedButtons(optionItems)
}


@Composable
private fun SetSegmentedButtons(
    optionItems: List<OptionItem>
) {
    val checkedList = remember { mutableStateListOf<Int>() }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MultiChoiceSegmentedButtonRow(
            modifier = Modifier
                .width(200.dp)  // Define the desired width
                .fillMaxWidth()  // Fill the width available in the Box
        ) {
            optionItems.forEachIndexed { index, option ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = optionItems.size
                    ),
                    icon = {
                        SegmentedButtonDefaults.Icon(active = index in checkedList) {
                            Icon(
                                imageVector = optionItems[index].icon,
                                contentDescription = null,
                                modifier = Modifier.size(SegmentedButtonDefaults.IconSize)
                            )
                        }
                    },
                    onCheckedChange = {
                        // Toggle the selection state
                        if (index in checkedList) {
                            //checkedList.remove(index)
                        } else {
                            checkedList.clear()
                            checkedList.add(index)
                        }

                        // OnClick call
                        option.onClick()
                    },
                    checked = index in checkedList
                ) {
                    Text(option.name)
                }
            }
        }
    }
}

//data class for options and icons
data class OptionItem(
    val name: String,
    val icon: ImageVector,
    val onClick: () -> Unit // Action to be performed when clicked
)


