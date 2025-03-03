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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import top.cha01.geofence.data.database.entities.RcConfig

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RcConfigListItem(
    config: RcConfig,
//    navigateToDetail: (Int) -> Unit,
    toggleSelection: (Int) -> Unit,
    upButtonClick: (Int) -> Unit,
    downButtonClick: (Int) -> Unit,
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
//                onClick = { navigateToDetail(config.Id) },
//                onLongClick = { toggleSelection(config.Id) }
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
                val clickModifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { toggleSelection(config.Id) }
                AnimatedContent(targetState = isSelected, label = "avatar") { selected ->
                    if (selected) {
                        SelectedProfileImage(clickModifier)
                    } else {
                        SingleChoiceSegmentedButtonRow {
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                                enabled = !config.Code.isNullOrBlank(),
                                onClick = { upButtonClick(config.Id) },
                                selected = false
                            ) {
                                Text("抬杆")
                            }
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                                enabled = !config.Code.isNullOrBlank(),
                                onClick = { downButtonClick(config.Id) },
                                selected = false
                            ) {
                                Text("落杆")
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = config.Name,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = config.Memo,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
//    }
}

@Composable
fun SelectedProfileImage(modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
