@file:OptIn(ExperimentalFoundationApi::class)

package com.norm.mymultiselectionlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MultiSelectionState(initialIsMultiSelectedModeEnabled: Boolean = false) {
    var isMultiSelectionModeEnabled by mutableStateOf(initialIsMultiSelectedModeEnabled)
}

object MultiSelectionStateSaver : Saver<MultiSelectionState, Boolean> {
    override fun restore(value: Boolean): MultiSelectionState? {
        return MultiSelectionState(value)
    }

    override fun SaverScope.save(value: MultiSelectionState): Boolean? {
        return value.isMultiSelectionModeEnabled
    }
}

@Composable
fun rememberMultiSelectionState(): MultiSelectionState {
    return rememberSaveable(saver = MultiSelectionStateSaver) {
        MultiSelectionState()
    }
}

@Composable
fun <T> MultiSelectionList(
    modifier: Modifier = Modifier,
    state: MultiSelectionState,
    items: List<T>,
    selectedItems: List<T>,
    itemContent: @Composable (T) -> Unit,
    key: ((T) -> Any)? = null,
    onClick: (T) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(
            items,
            key = key,
        ) { item ->
            MultiSelectedContainer(
                isEnabled = state.isMultiSelectionModeEnabled,
                isSelected = selectedItems.contains(item),
                multiSelectionModeChange = {
                    state.isMultiSelectionModeEnabled = it
                },
                onClick = {
                    onClick(item)
                }) {
                itemContent(item)
            }
        }
    }
}

@Composable
fun MultiSelectedContainer(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    isSelected: Boolean,
    multiSelectionModeChange: (Boolean) -> Unit,
    radioButtonBackgroundColor: Color = MaterialTheme.colorScheme.background,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 50.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    multiSelectionModeChange(true)
                    onClick()
                }
            ),
        contentAlignment = Alignment.CenterEnd,
    ) {
        content()
        AnimatedVisibility(
            modifier = Modifier
                .background(radioButtonBackgroundColor),
            visible = isEnabled,
            enter = slideInHorizontally(initialOffsetX = { it / 2 }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { it / 2 }) + fadeOut(),
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
            )
        }
    }
}