package com.mindlab.intrest_agent_android.presentation.orders.order

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.mindlab.intrest_agent_android.R
import com.mindlab.intrest_agent_android.data.network.response.IncomingResponse
import com.mindlab.intrest_agent_android.presentation.components.CustomInput
import com.mindlab.intrest_agent_android.presentation.components.HeaderTitle
import com.mindlab.intrest_agent_android.presentation.components.MultiStyleText
import com.mindlab.intrest_agent_android.presentation.components.PrimaryButton
import com.mindlab.intrest_agent_android.presentation.components.theme.GRAY_800
import com.mindlab.intrest_agent_android.presentation.components.util.FocusedTextFieldKey
import com.mindlab.intrest_agent_android.presentation.components.util.ScreenEvent
import com.mindlab.intrest_agent_android.utils.Constants
import com.mindlab.intrest_agent_android.utils.collectIn
import kotlinx.coroutines.flow.collect

/**
 * Created by Alireza Nezami on 1/2/2022.
 */
@ExperimentalAnimationApi
@Composable
fun PriceAdjustmentViews(
    modifier: Modifier,
    viewModel: OrderViewModel,
    state: OrderViewState
) {
    if (state.isAdjustmentPriceOpen()) {

        val invoice = (state as OrderViewState.PriceAdjustment).getInvoiceItem()
        val trackingNum = (state as OrderViewState.PriceAdjustment).getTrackingNumber()

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colors.secondary
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp)
            ) {
                Header(
                    modifier = Modifier
                        .fillMaxWidth(),
                    trackingNum = trackingNum,
                    onClick = { viewModel.onHidePriceAdjustment() }
                )

                Content(
                    modifier = Modifier
                        .fillMaxSize(),
                    viewModel = viewModel,
                    invoice = invoice
                ) { viewModel.onHidePriceAdjustment() }
            }
        }

    }
}

@Composable
fun Header(
    modifier: Modifier,
    trackingNum: Int,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier.padding(bottom = 16.dp)
    ) {

        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 10.dp)
                .clickable { onClick() },
            painter = painterResource(R.drawable.ic_left_arrow),
            tint = MaterialTheme.colors.onBackground,
            contentDescription = ""
        )

        HeaderTitle(
            modifier = Modifier
                .weight(1f),
            text = stringResource(R.string.price_adjustment),
            color = MaterialTheme.colors.onBackground
        )

        MultiStyleText(
            modifier = Modifier,
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = GRAY_800
                    )
                ) {
                    append(stringResource(R.string.tracking_num_is))
                }

                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colors.onSecondary
                    )
                ) {
                    append(" #$trackingNum")
                }

            }
        )

    }
}


@ExperimentalAnimationApi
@Composable
fun Content(
    modifier: Modifier,
    viewModel: OrderViewModel,
    invoice: IncomingResponse.Content.Invoice,
    oncBack: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val focusManager = LocalFocusManager.current

    val adjustmentPrice by viewModel.adjustmentPrice.collectAsState()
    val taxRate by viewModel.adjustTaxRate.collectAsState()
    val adjustDescription by viewModel.adjustDescription.collectAsState()
    val areInputsValid by viewModel.areInputsValid.collectAsState()

    val priceFocusRequester = remember { FocusRequester() }
    val taxFocusRequester = remember { FocusRequester() }


    val events = remember(viewModel.screenEvents, lifecycleOwner) {
        viewModel.screenEvents.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }

    LaunchedEffect(Unit) {
        events.collectIn(lifecycleOwner) { event ->
            when (event) {
                is ScreenEvent.ClearFocus -> focusManager.clearFocus()
                is ScreenEvent.RequestFocus -> {
                    when (event.textFieldKey) {
                        FocusedTextFieldKey.PRICE -> priceFocusRequester.requestFocus()
                        FocusedTextFieldKey.TAX -> taxFocusRequester.requestFocus()
                    }
                }
                is ScreenEvent.MoveFocus -> focusManager.moveFocus(event.direction)
            }
        }
    }


    Card(
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Column(modifier = Modifier.padding(24.dp)) {
                Row {
                    CustomInput(
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(priceFocusRequester),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        label = stringResource(id = R.string.adjustment_price),
                        placeHolderText = stringResource(id = R.string.dollar_sign),
                        inputWrapper = adjustmentPrice,
                        onValueChange = viewModel::onAdjustmentAmountEnter,
                        onImeKeyAction = viewModel::onNextImeActionClick
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    CustomInput(
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(taxFocusRequester),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        onImeKeyAction = viewModel::onDoneImeActionClick,
                        label = stringResource(id = R.string.tax_rate),
                        placeHolderText = stringResource(id = R.string.percent_sign),
                        inputWrapper = taxRate,
                        onValueChange = viewModel::onTaxRateEnter,
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                CustomInput(
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    onImeKeyAction = viewModel::onDoneImeActionClick,
                    label = stringResource(id = R.string.adjustment_description),
                    placeHolderText = stringResource(id = R.string.add_new_adjustment),
                    maxLength = Constants.MAX_MESSAGE_CHAR_LENGTH,
                    inputWrapper = adjustDescription,
                    onValueChange = viewModel::onAdjustmentDescriptionEnter,
                )


            }

            PrimaryButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(32.dp),
                text = stringResource(R.string.submit),
                enabled = areInputsValid,
                onClick = {
                    viewModel.onPriceAdjustmentSubmit(invoice)
                    oncBack()
                }
            )


        }
    }
}
