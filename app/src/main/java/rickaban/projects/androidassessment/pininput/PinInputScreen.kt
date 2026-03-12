package rickaban.projects.androidassessment.pininput

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dialpad
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import rickaban.projects.androidassessment.R

private const val PIN_LENGTH = 4
const val TAG_EDT_PIN_INPUT = "edt_pin_input"
const val TAG_BTN_PIN_SUBMIT = "btn_pin_submit"

/**
 * STATEFUL CONTAINER
 * Manages the PIN state, length constraints, and keyboard focus.
 */
@Composable
fun PinInputScreen(
    onPinSubmit: (String) -> Unit
) {
    var pin by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val submitPin = {
        if (pin.length == PIN_LENGTH) {
            focusManager.clearFocus()
            onPinSubmit(pin)
        }
    }

    PinInputContent(
        pin = pin,
        onPinChange = { newValue ->
            val sanitized = newValue.filter { it.isDigit() }.take(PIN_LENGTH)
            pin = sanitized
        },
        onSubmit = submitPin,
        modifier = Modifier
    )
}

/**
 * STATELESS UI
 * Purely declarative and highly testable.
 */
@Composable
private fun PinInputContent(
    pin: String,
    onPinChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.Dialpad,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.enter_pin),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(R.string.enter_pin_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = pin,
            onValueChange = onPinChange,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                letterSpacing = 8.sp,
                fontSize = 24.sp
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onSubmit() }
            ),
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .testTag(TAG_EDT_PIN_INPUT),
            shape = MaterialTheme.shapes.large
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSubmit,
            enabled = pin.length == PIN_LENGTH,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag(TAG_BTN_PIN_SUBMIT),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = stringResource(R.string.verify_pin),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}