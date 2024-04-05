package com.norm.mymultiselectionlist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.norm.mymultiselectionlist.ui.theme.MyMultiSelectionlistTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = rememberMultiSelectionState()
            val numbers = (1..100).toList()
            val selectedItems = remember {
                mutableStateListOf<Int>()
            }
            val context = LocalContext.current
            MyMultiSelectionlistTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                            ) {
                                TextButton(
                                    onClick = {
                                        state.isMultiSelectionModeEnabled =
                                            !state.isMultiSelectionModeEnabled
                                        if (state.isMultiSelectionModeEnabled) {
                                            selectedItems.clear()
                                        }
                                    }
                                ) {
                                    Text(
                                        text = if (state.isMultiSelectionModeEnabled) "Done"
                                        else "Select"
                                    )
                                }
                            }
                        }
                    ) { padding ->
                        MultiSelectionList(
                            modifier = Modifier
                                .padding(
                                    top = padding.calculateTopPadding(),
                                    bottom = padding.calculateBottomPadding(),
                                ),
                            state = state,
                            items = numbers,
                            selectedItems = selectedItems,
                            itemContent = {
                                Text(
                                    text = it.toString(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                )
                            },
                            key = {
                                it
                            },
                            onClick = {
                                if (state.isMultiSelectionModeEnabled) {
                                    if (selectedItems.contains(it)) {
                                        selectedItems.remove(it)
                                    } else {

                                        selectedItems.add(it)
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Click $it",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}