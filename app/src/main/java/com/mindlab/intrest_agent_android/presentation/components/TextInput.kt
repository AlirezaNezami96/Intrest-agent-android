package com.mindlab.intrest_agent_android.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.mindlab.intrest_agent_android.R
import com.mindlab.intrest_agent_android.presentation.components.theme.GRAY_300
import com.mindlab.intrest_agent_android.presentation.components.util.InputWrapper
import com.mindlab.intrest_agent_android.presentation.components.util.OnImeKeyAction
import com.mindlab.intrest_agent_android.presentation.components.util.OnValueChange
import com.mindlab.intrest_agent_android.utils.Constants

/**
 * Created by Alireza Nezami on 12/8/2021.
 */
@Composable
fun CustomInput(
    modifier: Modifier,
    inputWrapper: InputWrapper,
    onValueChange: OnValueChange,
    onImeKeyAction: OnImeKeyAction,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLength: Int = Constants.MAX_NUMBER_CHAR_LENGTH,
    trailingIcon: Int? = null,
    label: String?,
    placeHolderText: String,
) {
    val fieldValue = remember {
        mutableStateOf(TextFieldValue(inputWrapper.value, TextRange(inputWrapper.value.length)))
    }
    var passwordVisibility by remember { mutableStateOf(false) }


    Column(
        modifier = modifier
    ) {

        label?.let {
            CaptionTitle(
                modifier = Modifier
                    .padding(start = 8.dp),
                text = label,
                color = MaterialTheme.colors.secondaryVariant
            )
        }

        Spacer(Modifier.height(2.dp))

        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = fieldValue.value,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.secondary,
                textColor = MaterialTheme.colors.onSecondary,
                placeholderColor = MaterialTheme.colors.secondaryVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            onValueChange = {
                if (it.text.length <= maxLength) {
                    fieldValue.value = it
                    onValueChange(it.text)
                }
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(onAny = { onImeKeyAction() }),
            placeholder = {
                Text(text = placeHolderText)
            },
            singleLine = true,
            shape = RoundedCornerShape(50),
            visualTransformation = if (passwordVisibility) VisualTransformation.None
            else visualTransformation,
            trailingIcon = {
                if (visualTransformation == PasswordVisualTransformation()) {
                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(
                            painter =
                            if (passwordVisibility)
                                painterResource(R.drawable.ic_baseline_visibility_off)
                            else
                                painterResource(R.drawable.ic_baseline_visibility),
                            contentDescription = "",
                            tint =
                            if (passwordVisibility)
                                MaterialTheme.colors.onSecondary
                            else GRAY_300,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                } else if (trailingIcon != null) {
                    Image(
                        modifier = Modifier
                            .padding(end = 16.dp),
                        painter = painterResource(trailingIcon),
                        contentDescription = ""
                    )
                }
            }
        )

        Spacer(Modifier.height(2.dp))

        inputWrapper.error?.let {
            val density = LocalDensity.current

            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    initialOffsetY = { with(density) { -40.dp.roundToPx() } }
                ) + fadeIn(
                    initialAlpha = 0.3f
                ),
                exit = slideOutVertically() + fadeOut()
            ) {

                CaptionTitle(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .fillMaxWidth(),
                    text = inputWrapper.error,
                    color = MaterialTheme.colors.error
                )
            }
        }
    }
}


@Composable
fun MultiLineInput(
    modifier: Modifier,
    onValueChange: OnValueChange,
    placeHolderText: String = "",
) {

    val fieldValue = remember {
        mutableStateOf("")
    }
    TextField(
        modifier = modifier,
        value = fieldValue.value,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.secondary,
            textColor = MaterialTheme.colors.onSecondary,
            placeholderColor = MaterialTheme.colors.secondaryVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        onValueChange = {
            fieldValue.value = it
            onValueChange(it)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Default
        ),
        placeholder = {
            Text(text = placeHolderText)
        },
        singleLine = false,
        maxLines = 5,
        shape = RoundedCornerShape(10.dp),
    )


}

@Composable
fun PriceInput(
    modifier: Modifier,
    onValueChange: OnValueChange,
    placeHolderText: String = "",
    value: String = "",
    label: String? = "",
) {


    Column(
        modifier = modifier
    ) {

        label?.let {
            CaptionTitle(
                modifier = Modifier
                    .padding(start = 8.dp),
                text = label,
                color = MaterialTheme.colors.secondaryVariant
            )
        }

        Spacer(Modifier.height(2.dp))
        val fieldValue = remember {
            mutableStateOf(value)
        }

        TextField(
            modifier = Modifier
                .background(
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colors.secondary,
                ),
            value = fieldValue.value,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.secondary,
                textColor = MaterialTheme.colors.onSecondary,
                placeholderColor = MaterialTheme.colors.secondaryVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            onValueChange = {
                fieldValue.value = it
                onValueChange(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            placeholder = {
                Text(text = placeHolderText)
            },
            singleLine = true,
            maxLines = 1,
            shape = RoundedCornerShape(10.dp),
        )
    }

}


//
//@ExperimentalAnimationApi
//@Composable
//fun MainInput(
//    modifier: Modifier,
//    placeHolderText: String,
//    label: String?,
//    description: String?,
//    keyboardActions: KeyboardActions,
//    keyboardOptions: KeyboardOptions,
//    isPassword: Boolean? = false,
//    isDropDown: Boolean? = false,
//    isError: Boolean? = false,
//    focusRequester: FocusRequester,
//    onChange: (String) -> Unit,
//) {
//    val fieldValue = remember { mutableStateOf("") }
//    var passwordVisibility by remember { mutableStateOf(false) }
//    val hasError by rememberSaveable { mutableStateOf(isError) }
//    val density = LocalDensity.current
//
//    Column(
//        modifier = modifier
//            .then(
//                Modifier
//                    .padding(8.dp)
//                    .fillMaxWidth()
//            )
//    ) {
//        label?.let {
//            CaptionTitle(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                text = label,
//                color = MaterialTheme.colors.primaryVariant
//            )
//        }
//
//        TextField(
//            value = fieldValue.value,
//            onValueChange = {
//                fieldValue.value = it
//                onChange(it)
//            },
//            colors = TextFieldDefaults.textFieldColors(
//                backgroundColor = MaterialTheme.colors.secondary,
//                textColor = MaterialTheme.colors.onSecondary,
//                placeholderColor = MaterialTheme.colors.secondaryVariant,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent,
//                disabledIndicatorColor = Color.Transparent,
//            ),
//            modifier = Modifier
//                .fillMaxWidth()
//                .focusRequester(focusRequester)
//                .padding(vertical = 5.dp),
//            keyboardOptions = keyboardOptions,
//            keyboardActions = keyboardActions,
//            placeholder = {
//                Text(text = placeHolderText)
//            },
//            singleLine = true,
//            shape = RoundedCornerShape(40),
//            visualTransformation =
//            if (passwordVisibility || isPassword == false) VisualTransformation.None
//            else PasswordVisualTransformation(),
//            trailingIcon = {
//                if (isDropDown == true)
//                    Icon(
//                        imageVector = Icons.Filled.ArrowDropDown,
//                        contentDescription = "",
//                        tint = MaterialTheme.colors.onSecondary
//                    )
//                else if (isPassword == true) {
//                    IconButton(onClick = {
//                        passwordVisibility = !passwordVisibility
//                    }) {
//                        Icon(
//                            painter =
//                            if (passwordVisibility)
//                                painterResource(R.drawable.ic_baseline_visibility_off)
//                            else
//                                painterResource(R.drawable.ic_baseline_visibility),
//                            contentDescription = "",
//                            tint = MaterialTheme.colors.onSecondary
//                        )
//                    }
//                }
//            }
//        )
//
//        hasError?.let {
//            AnimatedVisibility(
//                visible = it,
//                enter = slideInVertically(
//                    initialOffsetY = { with(density) { -40.dp.roundToPx() } }
//                ) + fadeIn(
//                    // Fade in with the initial alpha of 0.3f.
//                    initialAlpha = 0.3f
//                ),
//                exit = slideOutVertically() + fadeOut()
//            ) {
//
//                description?.let {
//                    CaptionTitle(
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        text = description,
//                        color = MaterialTheme.colors.error
//                    )
//                }
//            }
//        }
//    }
//}
//
//@ExperimentalAnimationApi
//@Composable
//fun PasswordInput(
//    modifier: Modifier,
//    isError: Boolean? = false,
//    focusRequester: FocusRequester,
//    keyboardActions: KeyboardActions,
//    onChange: (String) -> Unit,
//) {
//    MainInput(
//        modifier = modifier,
//        placeHolderText = stringResource(R.string.password_placeholder),
//        label = stringResource(R.string.password_label),
//        description = stringResource(R.string.password_error),
//        keyboardActions = keyboardActions,
//        keyboardOptions = KeyboardOptions(
//            keyboardType = KeyboardType.Password,
//            imeAction = ImeAction.Done
//        ),
//        isError = isError,
//        isPassword = true,
//        focusRequester = focusRequester,
//        onChange = onChange
//    )
//}



