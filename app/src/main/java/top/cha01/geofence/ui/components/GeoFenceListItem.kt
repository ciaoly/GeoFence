package top.cha01.geofence.ui.components

/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.cha01.geofence.data.database.entities.GeoFence

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GeoFenceListItem(
    fence: GeoFence,
//    navigateToDetail: (Int) -> Unit,
//    toggleSelection: (Int) -> Unit,
//    modifier: Modifier = Modifier,
//    isOpened: Boolean = false,
    isSelected: Boolean = false,
) {
//    Card(
//        modifier = modifier
//            .padding(horizontal = 16.dp, vertical = 4.dp)
//            .semantics { selected = isSelected }
//            .clip(CardDefaults.shape)
//            .combinedClickable(
//                onClick = { navigateToDetail(fence.Id) },
//                onLongClick = { toggleSelection(fence.Id) }
//            )
//            .clip(CardDefaults.shape),
//        colors = CardDefaults.cardColors(
//            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
//            else if (isOpened) MaterialTheme.colorScheme.secondaryContainer
//            else MaterialTheme.colorScheme.surfaceVariant
//        )
//    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                AnimatedContent(targetState = isSelected, label = "avatar") { selected ->
                    if (selected) {
                        SelectedProfileImage(Modifier)
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = fence.Name,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = fence.Memo,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
//    }
}
