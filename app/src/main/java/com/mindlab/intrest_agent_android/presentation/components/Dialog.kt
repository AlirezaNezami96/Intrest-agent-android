package com.mindlab.intrest_agent_android.presentation.components

import android.widget.NumberPicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import com.mindlab.intrest_agent_android.R
import com.mindlab.intrest_agent_android.data.network.response.DeliverCostResponse
import com.mindlab.intrest_agent_android.presentation.cost.CostViewModel
import com.mindlab.intrest_agent_android.presentation.menu.MenuViewModel
import timber.log.Timber
import kotlin.random.Random

/**
 * Created by Alireza Nezami on 12/29/2021.
 */

@Composable
fun RejectReasonDialog(
    modifier: Modifier,
    rejectionItemId: Int,
    onSubmit: (text: String, id: Int) -> Unit,
    onDismiss: () -> Unit
) {

    var selectedIndex by remember { mutableStateOf(0) }
    var selectedReason by remember { mutableStateOf("") }
    var showOtherMessage by remember { mutableStateOf(false) }

    if (rejectionItemId > -1) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = null,
            text = null,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            buttons = {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    HeaderTitle(
                        modifier = Modifier,
                        text = stringResource(R.string.reject_reason)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val rejectReasons = listOf(
                        stringResource(R.string.unavailable_ingredient),
                        stringResource(R.string.too_far_address),
                        stringResource(R.string.insufficent_quantity),
                        stringResource(R.string.closing_soon),
                        stringResource(R.string.busy),
                        stringResource(R.string.other_reason),
                    )

                    rejectReasons.forEachIndexed { index, s ->
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (index == (rejectReasons.size) - 1)
                                    showOtherMessage = true
                                else {
                                    selectedReason = s
                                    showOtherMessage = false
                                }
                                selectedIndex = index

                            }
                        ) {

                            RadioButton(
                                selected = selectedIndex == index,
                                onClick = {
                                    if (index == (rejectReasons.size) - 1)
                                        showOtherMessage = true
                                    else {
                                        selectedReason = s
                                        showOtherMessage = false
                                    }
                                    selectedIndex = index

                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colors.primary,
                                    unselectedColor = MaterialTheme.colors.secondaryVariant,
                                )
                            )

                            CaptionTitle(
                                modifier = Modifier
                                    .padding(start = 2.dp)
                                    .align(Alignment.CenterVertically),
                                text = s,
                                color = MaterialTheme.colors.onSecondary
                            )
                        }

//                        Spacer(modifier = Modifier.height(5.dp))

                    }

                    if (showOtherMessage) {
                        Spacer(modifier = Modifier.height(16.dp))

                        CaptionTitle(
                            modifier = Modifier.padding(start = 8.dp),
                            text = stringResource(R.string.message)
                        )

                        Spacer(modifier = Modifier.height(8.dp))


                        MultiLineInput(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(100.dp, 300.dp)
                                .align(Alignment.CenterHorizontally),
                            onValueChange = { selectedReason = it },
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.align(Alignment.End),
                        horizontalArrangement = Arrangement.End
                    ) {
                        BorderButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            text = stringResource(R.string.cancel),
                            onClick = { onDismiss() }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        PrimaryButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            text = stringResource(R.string.submit),
                            onClick = {
                                onDismiss()
                                onSubmit(selectedReason, rejectionItemId)
                            }
                        )
                    }


                }
            }
        )
    }
}

@Composable
fun AdjustPriceDialog(
    modifier: Modifier,
    itemId: Int,
    viewModel: MenuViewModel,
    onDismiss: () -> Unit
) {

    val price by viewModel.dialogPrice.collectAsState()

    if (itemId > -1) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = null,
            text = null,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            buttons = {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    HeaderTitle(
                        modifier = Modifier,
                        text = stringResource(R.string.adjustment_price)
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    CustomInput(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                        onImeKeyAction = {},
                        visualTransformation = VisualTransformation.None,
                        label = stringResource(id = R.string.adjustment_amount),
                        placeHolderText = stringResource(R.string.dollar_sign),
                        inputWrapper = price,
                        onValueChange = {
                            price.value = it
                            viewModel.onPriceEnter(it)
                        },
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BorderButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            text = stringResource(R.string.cancel),
                            onClick = { onDismiss() }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        PrimaryButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            text = stringResource(R.string.submit),
                            enabled = price.error == null,
                            onClick = {
                                onDismiss()
                                viewModel.onAdjustPrice(itemId)
                            }
                        )
                    }


                }
            }
        )
    }
}

@Composable
fun PreparationTimeDialog(
    modifier: Modifier,
    itemId: Int,
    viewModel: MenuViewModel,
    onDismiss: () -> Unit
) {

    val time by viewModel.dialogTime.collectAsState()

    if (itemId > -1) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = null,
            text = null,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            buttons = {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    HeaderTitle(
                        modifier = Modifier,
                        text = stringResource(R.string.preparation_time)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CaptionTitle(
                        modifier = Modifier,
                        text = stringResource(R.string.you_can_change_time),
                        color = MaterialTheme.colors.onSecondary
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    CustomInput(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                        onImeKeyAction = {},
                        visualTransformation = VisualTransformation.None,
                        label = stringResource(id = R.string.preparation_time),
                        placeHolderText = stringResource(R.string.set_time),
                        trailingIcon = R.drawable.ic_time_filled,
                        inputWrapper = time,
                        onValueChange = {
                            time.value = it
                            viewModel.onTimeEnter(it)
                        },
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BorderButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            text = stringResource(R.string.cancel),
                            onClick = { onDismiss() }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        PrimaryButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            text = stringResource(R.string.submit),
                            enabled = time.error == null,
                            onClick = {
                                onDismiss()
                                viewModel.onPreparationTime(itemId)
                            }
                        )
                    }


                }
            }
        )
    }
}

@Composable
fun EditCostDialog(
    modifier: Modifier,
    item: DeliverCostResponse.Embedded.Cost,
    onDismiss: () -> Unit,
    onPositiveDismiss: () -> Unit,
    viewModel: CostViewModel
) {

    if (item.id != 0) {
        val costWrapper by viewModel.dialogCost.collectAsState()
        val lowerWrapper by viewModel.dialogLower.collectAsState()
        val upperWrapper by viewModel.dialogUpper.collectAsState()
        val areInputsValid by viewModel.areInputsValid.collectAsState()

        costWrapper.value = item.cost.toString()
        lowerWrapper.value = item.lowerRadius.toString()
        upperWrapper.value = item.upperRadius.toString()

        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = null,
            text = null,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            buttons = {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    HeaderTitle(
                        modifier = Modifier,
                        text = stringResource(R.string.edit_item)
                    )

                    Spacer(modifier = Modifier.height(42.dp))

                    CustomInput(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),

                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                        onImeKeyAction = {},
                        visualTransformation = VisualTransformation.None,
                        label = stringResource(id = R.string.cost),
                        placeHolderText = stringResource(id = R.string.dollar_sign),
                        inputWrapper = costWrapper,
                        onValueChange = {
                            costWrapper.value = it
                            viewModel.onCostEnter(it)
                        },
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    CustomInput(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),

                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                        onImeKeyAction = {},
                        visualTransformation = VisualTransformation.None,
                        label = stringResource(id = R.string.lower_radius),
                        placeHolderText = stringResource(id = R.string.km),
                        inputWrapper = lowerWrapper,
                        onValueChange = {
                            lowerWrapper.value = it
                            viewModel.onLowerEnter(it)
                        },
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    CustomInput(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),

                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                        onImeKeyAction = {},
                        visualTransformation = VisualTransformation.None,
                        label = stringResource(id = R.string.upper_radius),
                        placeHolderText = stringResource(id = R.string.km),
                        inputWrapper = upperWrapper,
                        onValueChange = {
                            upperWrapper.value = it
                            viewModel.onUpperEnter(it)
                        },
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BorderButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            text = stringResource(R.string.cancel),
                            onClick = {
                                onDismiss()
                            }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        PrimaryButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            text = stringResource(R.string.update),
                            enabled = areInputsValid,
                            onClick = {
                                onPositiveDismiss()
                                viewModel.updateItem(
                                    costWrapper.value,
                                    lowerWrapper.value,
                                    upperWrapper.value,
                                    item.id
                                )
                            }
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun AddCostDialog(
    modifier: Modifier,
    onDismiss: () -> Unit,
    onPositiveDismiss: () -> Unit,
    show: Boolean,
    viewModel: CostViewModel
) {

    if (show) {
        val costWrapper by viewModel.dialogCost.collectAsState()
        val lowerWrapper by viewModel.dialogLower.collectAsState()
        val upperWrapper by viewModel.dialogUpper.collectAsState()
        val areInputsValid by viewModel.areInputsValid.collectAsState()

        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = null,
            text = null,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            buttons = {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    HeaderTitle(
                        modifier = Modifier,
                        text = stringResource(R.string.add_new_item)
                    )

                    Spacer(modifier = Modifier.height(42.dp))

                    CustomInput(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                        onImeKeyAction = {},
                        visualTransformation = VisualTransformation.None,
                        label = stringResource(id = R.string.cost),
                        placeHolderText = stringResource(id = R.string.dollar_sign),
                        inputWrapper = costWrapper,
                        onValueChange = viewModel::onCostEnter,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    CustomInput(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                        onImeKeyAction = {},
                        visualTransformation = VisualTransformation.None,
                        label = stringResource(id = R.string.lower_radius),
                        placeHolderText = stringResource(id = R.string.km),
                        inputWrapper = lowerWrapper,
                        onValueChange = viewModel::onLowerEnter,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    CustomInput(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                        onImeKeyAction = {},
                        visualTransformation = VisualTransformation.None,
                        label = stringResource(id = R.string.upper_radius),
                        placeHolderText = stringResource(id = R.string.km),
                        inputWrapper = upperWrapper,
                        onValueChange = viewModel::onUpperEnter,
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BorderButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            text = stringResource(R.string.cancel),
                            onClick = { onDismiss() }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        PrimaryButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            text = stringResource(R.string.submit),
                            enabled = areInputsValid,
                            onClick = {
                                onPositiveDismiss()
                                viewModel.addCost(Random.nextInt(0, 100))
                            }
                        )
                    }


                }
            }
        )
    }
}


@Composable
fun DeleteCostDialog(
    modifier: Modifier,
    itemId: Int,
    onSubmit: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    if (itemId > -1) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = null,
            text = null,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            buttons = {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {

                    Spacer(modifier = Modifier.height(48.dp))

                    H6Title(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 8.dp,
                            ),
                        text = stringResource(R.string.are_you_sure_to_delete),
                        textAlign = TextAlign.Center
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(
                                bottom = 60.dp,
                                end = 48.dp,
                                start = 48.dp,
                                top = 66.dp
                            ),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        PrimaryButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            text = stringResource(R.string.no),
                            onClick = {
                                onDismiss()
                            }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        BorderButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            text = stringResource(R.string.yes),
                            onClick = {
                                onSubmit(itemId)
                                onDismiss()
                            }
                        )

                    }


                }
            }
        )
    }
}

@Composable
fun NumberPicker(
    modifier: Modifier,
    itemId: Int,
    onSubmit: (text: String, id: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val minutes = "Minutes"
    val numberPickerValues = arrayOf(
        "5 $minutes",
        "10  $minutes",
        "15  $minutes",
        "20  $minutes",
        "25  $minutes",
        "30  $minutes"
    )
    var selectedDelay = numberPickerValues[0]

    if (itemId != -1) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = null,
            text = null,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            buttons = {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    HeaderTitle(
                        modifier = Modifier,
                        text = stringResource(R.string.time_adjustment)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CaptionTitle(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.delay_dialog_description),
                        color = MaterialTheme.colors.secondaryVariant
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    AndroidView(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        factory = { context ->
                            NumberPicker(context).apply {

                                minValue = 0
                                maxValue = numberPickerValues.size - 1
                                displayedValues = numberPickerValues
                                wrapSelectorWheel = true
                                setOnValueChangedListener { numberPicker, i, i2 ->
                                    Timber.d("number picker value is: ${numberPickerValues[i2]}")
                                    selectedDelay = numberPickerValues[i2]
                                }
                            }
                        })

                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        BorderButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            text = stringResource(R.string.cancel),
                            onClick = { onDismiss() }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        PrimaryButton(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            text = stringResource(R.string.submit),
                            onClick = {
                                onDismiss()
                                onSubmit(selectedDelay, itemId)
                            }
                        )
                    }
                }
            }
        )
    }
}