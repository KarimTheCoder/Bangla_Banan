package com.google.mlkit.samples.vision.digitalink.kotlin.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope


@Composable
fun MyDrawerLayout(
    scaffoldContent: @Composable (drawerState: DrawerState, scope: CoroutineScope) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf<DrawerItem?>(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(drawerState, selectedItem, onItemSelect = { selectedItem = it }) }
    ) {
        scaffoldContent(drawerState, scope)
    }
}


//// Function to handle drawer content
//@Composable
//fun DrawerContent(drawerState: DrawerState, selectedItem: DrawerItem?, onItemSelect: (DrawerItem) -> Unit) {
//    val drawerItems = getDrawerItems()
//
//    ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
//        Text("Folders", modifier = Modifier.padding(16.dp))
//        HorizontalDivider()
//        drawerItems.forEach { item ->
//            NavigationDrawerItem(
//                label = {
//                    DrawerItemLabel(item)
//                },
//                selected = selectedItem == item,
//                icon = item.icon,
//                onClick = { onItemSelect(item) }
//            )
//        }
//    }
//}

@Composable
fun DrawerContent(
    drawerState: DrawerState,
    selectedItem: DrawerItem?,
    onItemSelect: (DrawerItem) -> Unit
    //, onAddClick: () -> Unit // Lambda for handling Add button click
) {
    val drawerItems = getDrawerItems()

    ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween // Top for items, bottom for Add button
        ) {
            Column {
                Text("Folders", modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { DrawerItemLabel(item) },
                        selected = selectedItem == item,
                        icon = item.icon,
                        onClick = { onItemSelect(item) }
                    )
                }
            }

            // Add button at the bottom
            Button(
                onClick = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Add folder")
            }
        }
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
fun getDrawerItems(): List<DrawerItem> {

    return listOf(
        DrawerItem(title = "Class 10", icon = { Icon(Icons.Default.Menu, contentDescription = "Menu") }, trailingText = "5"),
        DrawerItem(title = "Settings", icon = { Icon(Icons.Default.Menu, contentDescription = "Settings") }, trailingText = "3"),
        DrawerItem(title = "About", icon = { Icon(Icons.Default.Menu, contentDescription = "About") }, trailingText = "10")
    )
}


// Data class to represent each Drawer item
data class DrawerItem(val title: String, val icon: @Composable () -> Unit, val trailingText: String?)

