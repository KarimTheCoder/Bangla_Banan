package com.google.mlkit.samples.vision.digitalink.kotlin.ui.screens.lesson.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.mlkit.samples.vision.digitalink.R
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.activity.InsertSampleData
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.AppDatabaseViewModel
import com.google.mlkit.samples.vision.digitalink.kotlin.ui.data.local.room.Folder
import kotlinx.coroutines.CoroutineScope


@Composable
fun HomeDrawer(
    viewModel: AppDatabaseViewModel,
    scaffoldContent: @Composable (drawerState: DrawerState, scope: CoroutineScope) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf<DrawerItem?>(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(drawerState, selectedItem, onItemSelect = { selectedItem = it },viewModel) }
    ) {
        scaffoldContent(drawerState, scope)
    }
}

@Composable
fun DrawerContent(
    drawerState: DrawerState,
    selectedItem: DrawerItem?,
    onItemSelect: (DrawerItem) -> Unit,
    viewModel: AppDatabaseViewModel
) {
    val items by viewModel.allFolders.observeAsState(emptyList())
    val drawerItems = getDrawerItems(items)

    if(!viewModel.isFolderIdInitialized() && drawerItems.isNotEmpty()){
        viewModel.setFolderId(drawerItems[0].folderId)
    }else{

        Text(text = "No drawer folder")
    }



    ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Scrollable folder items in LazyColumn
            LazyColumn(modifier = Modifier.weight(1f)) {
                item {
                    Text("Folders", modifier = Modifier.padding(16.dp))
                    HorizontalDivider()
                }
                items(drawerItems) { item ->
                    NavigationDrawerItem(
                        label = { DrawerItemLabel(item) },
                        selected = selectedItem == item,
                        icon = item.icon,
                        onClick = {

                            onItemSelect(item)
                            viewModel.setFolderId(item.folderId)

                        }
                    )
                }
            }

            // Add folder button fixed at the bottom
            HorizontalDivider(Modifier.height(1.dp), color = Color.LightGray)

            val isDialogOpen = remember { mutableStateOf(false) }
            val folderName = remember { mutableStateOf("") }


            InsertSampleData(viewModel = viewModel, LocalContext.current)

            Button(
                onClick = { isDialogOpen.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Add folder")
            }

            // Show the input dialog if needed
            if (isDialogOpen.value) {
                FolderNameInputDialog(
                    isDialogOpen = isDialogOpen,
                    folderName = folderName,
                    onConfirm = {




                        val folder = Folder(folderName = folderName.value)
                        viewModel.insertFolder(folder)
                        viewModel.fetchAllFolders()
                        folderName.value = "" // Clear input after saving
                    }
                )
            }
        }
    }
}



@Composable
fun FolderNameInputDialog(
    isDialogOpen: MutableState<Boolean>,
    folderName: MutableState<String>,
    onConfirm: () -> Unit
) {
    if (isDialogOpen.value) {
        AlertDialog(
            onDismissRequest = { isDialogOpen.value = false },
            title = { Text("Enter Folder Name") },
            text = {
                OutlinedTextField(
                    value = folderName.value,
                    onValueChange = { folderName.value = it },
                    label = { Text("Folder Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()
                        isDialogOpen.value = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { isDialogOpen.value = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


// Separate drawer item label logic
@Composable
fun DrawerItemLabel(item: DrawerItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = item.title, modifier = Modifier.weight(1f))
        if (item.trailingText?.isNotEmpty() == true) {
            Text(text = item.trailingText, modifier = Modifier.padding(end = 8.dp))
        }
    }
}




// A function to provide drawer items
fun getDrawerItems(items: List<Folder>): List<DrawerItem> {

    val list = mutableListOf<DrawerItem>()


    items.forEach {

        list.add(
            DrawerItem(it.folderName,it.folderId,
                icon = { Icon(painter = painterResource(R.drawable.outline_folder_24), contentDescription = "Menu") }, trailingText = "5"))


    }



    return list

}


// Data class to represent each Drawer item
data class DrawerItem(val title: String, val folderId: Long, val icon: @Composable () -> Unit, val trailingText: String?)
