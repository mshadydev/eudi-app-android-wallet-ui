/*
 * Copyright (c) 2023 European Commission
 *
 * Licensed under the EUPL, Version 1.2 or - as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence"); You may not use this work
 * except in compliance with the Licence.
 *
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the Licence is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the Licence for the specific language
 * governing permissions and limitations under the Licence.
 */

package eu.europa.ec.uilogic.component.wrap

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import eu.europa.ec.resourceslogic.theme.values.backgroundPaper
import eu.europa.ec.resourceslogic.theme.values.textPrimaryDark
import eu.europa.ec.resourceslogic.theme.values.textSecondaryDark
import eu.europa.ec.uilogic.component.AppIcons
import eu.europa.ec.uilogic.component.IconData
import eu.europa.ec.uilogic.component.preview.PreviewTheme
import eu.europa.ec.uilogic.component.preview.ThemeModePreviews
import eu.europa.ec.uilogic.component.utils.SIZE_SMALL
import eu.europa.ec.uilogic.component.utils.SPACING_EXTRA_LARGE
import eu.europa.ec.uilogic.component.utils.SPACING_EXTRA_SMALL
import eu.europa.ec.uilogic.component.utils.SPACING_LARGE
import eu.europa.ec.uilogic.component.utils.VSpacer
import eu.europa.ec.uilogic.extension.throttledClickable

data class OptionListItemUi(
    val text: String,
    val icon: IconData = AppIcons.KeyboardArrowRight,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WrapModalBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    shape: Shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    dragHandle: @Composable (() -> Unit)? = null,
    sheetContent: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = sheetState,
        shape = shape,
        dragHandle = dragHandle,
        content = sheetContent
    )
}

@Composable
fun GenericBaseSheetContent(
    title: String,
    bodyContent: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .background(color = MaterialTheme.colorScheme.backgroundPaper)
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 48.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.textPrimaryDark
            )
        )
        VSpacer.Small()
        bodyContent()
    }
}

@Composable
fun GenericBaseSheetContent(
    titleContent: @Composable () -> Unit,
    bodyContent: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .background(color = MaterialTheme.colorScheme.backgroundPaper)
            .fillMaxWidth()
            .padding(
                start = SPACING_LARGE.dp,
                top = SPACING_LARGE.dp,
                end = SPACING_LARGE.dp,
                bottom = SPACING_EXTRA_LARGE.dp
            )
    ) {
        titleContent()
        VSpacer.Large()
        bodyContent()
    }
}

@Composable
fun DialogBottomSheet(
    title: String,
    message: String,
    positiveButtonText: String? = null,
    negativeButtonText: String? = null,
    onPositiveClick: () -> Unit? = {},
    onNegativeClick: () -> Unit? = {}
) {
    GenericBaseSheetContent(
        title = title,
        bodyContent = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.textSecondaryDark
                )
            )
            VSpacer.Large()
            positiveButtonText?.let {
                WrapPrimaryButton(
                    onClick = { onPositiveClick.invoke() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true
                ) {
                    Text(
                        text = positiveButtonText
                    )
                }
            }
            VSpacer.Medium()
            negativeButtonText?.let {
                WrapSecondaryButton(
                    onClick = { onNegativeClick.invoke() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = true
                ) {
                    Text(
                        text = negativeButtonText
                    )
                }
            }
        }
    )
}

@Composable
fun BottomSheetWithOptionsList(
    title: String,
    message: String,
    options: List<OptionListItemUi>,
) {
    if (options.isNotEmpty()){
        GenericBaseSheetContent(
            title = title,
            bodyContent = {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.textSecondaryDark
                    )
                )
                VSpacer.Large()

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    OptionsList(
                        optionItems = options,
                    )
                }
            }
        )
    }
}

@Composable
fun OptionsList(
    optionItems: List<OptionListItemUi>,
) {
    LazyColumn {
        items(optionItems) { item ->
            OptionListItem(
                item = item,
                onItemSelected = {
                    item.onClick()
                },
            )
        }
    }
}

@Composable
fun OptionListItem(
    item: OptionListItemUi,
    onItemSelected: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(SIZE_SMALL.dp))
            .throttledClickable {
                onItemSelected.invoke()
            }
            .padding(vertical = SPACING_EXTRA_SMALL.dp)
        ,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = item.text,
            style = MaterialTheme.typography.bodyMedium
        )
        WrapIcon(
            modifier = Modifier.wrapContentWidth(),
            iconData = item.icon,
            customTint = MaterialTheme.colorScheme.primary
        )
    }
}

@ThemeModePreviews
@Composable
private fun DialogBottomSheetPreview() {
    PreviewTheme {
        DialogBottomSheet(
            title = "Title",
            message = "Message",
            positiveButtonText = "OK",
            negativeButtonText = "Cancel"
        )
    }
}

@ThemeModePreviews
@Composable
private fun BottomSheetWithOptionsListPreview() {
    PreviewTheme {
        BottomSheetWithOptionsList(
            title = "Title",
            message = "Message",
            options = listOf(
                OptionListItemUi(
                    text = "Small Name",
                    onClick = {}
                ),
                OptionListItemUi(
                    text = "MediumMediumMediumMedium Name",
                    onClick = {}
                ),
                OptionListItemUi(
                    text = "LargeLargeLargeLargeLargeLargeLargeLargeLargeLargeLargeLargeLargeLargeLargeLargeLargeLargeLargeLargeLargeLargeLargeLargeLargeLarge Name",
                    onClick = {}
                ),
            ),
        )
    }
}