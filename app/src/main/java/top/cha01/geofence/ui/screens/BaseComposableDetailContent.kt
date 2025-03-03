package top.cha01.geofence.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

abstract class BaseComposableDetailContent<T, V: ViewModel>(protected val viewModel: V) {
    @OptIn(ExperimentalSharedTransitionApi::class)

    val ComposableScreen: detailContentType = @Composable {
        selectedId: Int,
        modifier: Modifier,
        isListAndDetailVisible: Boolean,
        isDetailVisible: Boolean,
        sharedTransitionScope: SharedTransitionScope,
        animatedVisibilityScope: AnimatedVisibilityScope
    ->
        val itemData = itemDataFromViewModal(selectedId)
        Scaffold(

        ) { padding ->
            buildItemContent(
                    itemData,
                    modifier = modifier.padding(padding),
                    isListAndDetailVisible,
                    isDetailVisible,
                    sharedTransitionScope,
                    animatedVisibilityScope
                )
            }
    }

    @Composable
    abstract fun itemDataFromViewModal(id: Int): T

    @Composable
    @OptIn(ExperimentalSharedTransitionApi::class)
    abstract fun buildItemContent(itemData: T,
                             modifier: Modifier,
                             isListAndDetailVisible: Boolean,
                             isDetailVisible: Boolean,
                             sharedTransitionScope: SharedTransitionScope,
                             animatedVisibilityScope: AnimatedVisibilityScope
    )
}