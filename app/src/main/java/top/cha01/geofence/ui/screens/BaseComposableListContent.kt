package top.cha01.geofence.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

abstract class BaseComposableListContent<T: BaseListItemType, V: ViewModel>(protected val viewModel: V) {

    @OptIn(ExperimentalSharedTransitionApi::class)
    val ComposableScreen: listContentType = @Composable {
        selectionState: SelectionVisibilityState,
        onAdd: () -> Unit,
        onItemClick: (index: Int, itemId: Int) -> Unit,
        modifier: Modifier,
        isListAndDetailVisible: Boolean,
        isListVisible: Boolean,
        sharedTransitionScope: SharedTransitionScope,
        animatedVisibilityScope: AnimatedVisibilityScope
     ->
        val list = itemDataFromViewModal()

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = onAdd) {
                    Icon(Icons.Default.Add, contentDescription = "添加")
                }
            }
        ) {
                padding -> LazyColumn(
            modifier = Modifier.fillMaxHeight().padding(padding),
            contentPadding = PaddingValues(6.dp)
        ) {
            itemsIndexed(list) { index, item ->
                val interactionModifier = when (selectionState) {
                    SelectionVisibilityState.NoSelection -> {
                        Modifier.clickable(
                            onClick = { onItemClick(index, item.Id) }
                        )
                    }

                    is SelectionVisibilityState.ShowSelection -> {
                        Modifier.selectable(
                            selected = index == selectionState.selectedWordIndex,
                            onClick = { onItemClick(index, item.Id) }
                        )
                    }
                }


                val containerColor = when (selectionState) {
                    SelectionVisibilityState.NoSelection -> MaterialTheme.colorScheme.surface
                    is SelectionVisibilityState.ShowSelection ->
                        if (index == selectionState.selectedWordIndex) {
                            MaterialTheme.colorScheme.surfaceVariant
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                }
                val borderStroke = when (selectionState) {
                    SelectionVisibilityState.NoSelection -> BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline
                    )

                    is SelectionVisibilityState.ShowSelection ->
                        if (index == selectionState.selectedWordIndex) {
                            null
                        } else {
                            BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.outline
                            )
                        }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = containerColor),
                    border = borderStroke,
                    modifier = Modifier
                        .then(interactionModifier)
                        .fillMaxWidth()
                ) {
                    buildItemContent(
                        item,
                        modifier = Modifier.then(interactionModifier),
                        isListAndDetailVisible,
                        isListVisible,
                        sharedTransitionScope,
                        animatedVisibilityScope
                    )
                }
            }
        }
        }
    }

    @Composable
    abstract fun itemDataFromViewModal(): List<T>

    @Composable
    @OptIn(ExperimentalSharedTransitionApi::class)
    abstract fun buildItemContent(itemData: T,
                                  modifier: Modifier,
                                  isListAndDetailVisible: Boolean,
                                  isListVisible: Boolean,
                                  sharedTransitionScope: SharedTransitionScope,
                                  animatedVisibilityScope: AnimatedVisibilityScope
    )
}

/**
 * The description of the selection state for the [ListContent]
 */
sealed interface SelectionVisibilityState {

    /**
     * No selection should be shown, and each item should be clickable.
     */
    object NoSelection : SelectionVisibilityState

    /**
     * Selection state should be shown, and each item should be selectable.
     */
    data class ShowSelection(
        /**
         * The index of the word that is selected.
         */
        val selectedWordIndex: Int
    ) : SelectionVisibilityState
}
