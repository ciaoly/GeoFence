package top.cha01.geofence.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class BaseComposableListContent<T: BaseListItemType, V: ViewModel>(protected val viewModel: V) {

    open val actionButtonIcon: () -> @Composable () -> Unit =  { @Composable { Icon(Icons.Default.Add, contentDescription = "添加")} }

    @OptIn(ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class)
    @Composable
    fun ComposableScreen(
        onAdd: () -> Unit,
        onItemClick: (index: Int, itemId: Int) -> Unit,
        modifier: Modifier,
        isListAndDetailVisible: Boolean,
        isListVisible: Boolean,
        sharedTransitionScope: SharedTransitionScope,
        animatedVisibilityScope: AnimatedVisibilityScope
    ) {
        val list by listDataFlowFromViewModal().collectAsState(initial = emptyList())
        val selectedList = remember { mutableStateListOf<T>() }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = onAdd) {
                    actionButtonIcon()
                }
            }
        ) {
                padding -> LazyColumn(
            modifier = Modifier.fillMaxHeight().padding(padding),
            contentPadding = PaddingValues(6.dp)
        ) {
            itemsIndexed(list) { index, item ->
                var isSelected by remember { mutableStateOf(false) }
                val toggleSelection: (T) -> Unit = { item ->
                    isSelected = true
                    selectedList.add(item)
                }

                Card(
                    modifier = modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .semantics { selected = isSelected }
                        .clip(CardDefaults.shape)
                        .combinedClickable(
                            onClick = {
                                if (selectedList.size <= 0) {
                                    onItemClick(index, item.Id)
                                } else if (!isSelected) {
                                    toggleSelection(item)
                                } else {
                                    selectedList.remove(item)
                                    isSelected = false
                                }},
                            onLongClick = { toggleSelection(item) }
                        )
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
//                        else if (isOpened) MaterialTheme.colorScheme.secondaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    border = if (isSelected) BorderStroke( 1.dp, MaterialTheme.colorScheme.outline ) else null
                ) {
                    buildItemContent(
                        item,
                        modifier = Modifier,
                        isListAndDetailVisible,
                        isListVisible,
                        isSelected,
                        sharedTransitionScope,
                        animatedVisibilityScope
                    )
                }
            }
        }
        }
    }

    @Composable
    abstract fun listDataFlowFromViewModal(): StateFlow<List<T>>

    @Composable
    @OptIn(ExperimentalSharedTransitionApi::class)
    abstract fun buildItemContent(itemData: T,
                                  modifier: Modifier,
                                  isListAndDetailVisible: Boolean,
                                  isListVisible: Boolean,
                                  isSelected: Boolean,
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
