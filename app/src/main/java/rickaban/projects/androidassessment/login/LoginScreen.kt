package rickaban.projects.androidassessment.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rickaban.projects.androidassessment.R

private const val MIN_PASSWORD_LENGTH = 4
const val REQUIRED_PASSWORD = "Test@2026"
const val TAG_EDT_LOGIN_PASSWORD = "edt_login_password"
const val TAG_BTN_LOGIN_SUBMIT = "btn_login_submit"

/**
 * STATEFUL CONTAINER
 * Handles all business logic, state retention, and focus management.
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var password by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var isAuthenticating by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    val validateAndLogin = {
        isError = false
        focusManager.clearFocus()
        coroutineScope.launch {
            delay(2000)
            isAuthenticating = false
            if (password == REQUIRED_PASSWORD) {
                onLoginSuccess()
            } else {
                isError = true
            }
        }
        isAuthenticating = true
    }

    LoginContent(
        password = password,
        isError = isError,
        passwordVisible = passwordVisible,
        isLoginEnabled = password.length > MIN_PASSWORD_LENGTH,
        isAuthenticating = isAuthenticating,
        onPasswordChange = {
            password = it
            if (isError) isError = false
        },
        onToggleVisibility = { passwordVisible = !passwordVisible },
        onLoginSubmit = validateAndLogin,
        modifier = modifier
    )
}

/**
 * STATELESS UI
 * Purely declarative. 100% immune to side-effects and incredibly easy to screenshot test
 * because you can inject dummy data directly into these parameters.
 */
@Composable
private fun LoginContent(
    password: String,
    isError: Boolean,
    passwordVisible: Boolean,
    isLoginEnabled: Boolean,
    isAuthenticating: Boolean,
    onPasswordChange: (String) -> Unit,
    onToggleVisibility: () -> Unit,
    onLoginSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(8.dp)
    ) {
        ElevatedCard(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                modifier = modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderIcon()
                Spacer(modifier = Modifier.height(16.dp))
                HeaderText()
                SubtitleText()
                Spacer(modifier = Modifier.height(32.dp))
                PasswordField(
                    password = password,
                    isError = isError,
                    passwordVisible = passwordVisible,
                    onPasswordChange = onPasswordChange,
                    onToggleVisibility = onToggleVisibility,
                    onLoginSubmit = onLoginSubmit
                )
                Spacer(modifier = Modifier.height(16.dp))
                LoginButton(
                    isLoginEnabled = isLoginEnabled,
                    isAuthenticating = isAuthenticating,
                    onLoginSubmit = onLoginSubmit
                )
            }
        }
    }
}

@Composable
private fun HeaderIcon() {
    Icon(
        imageVector = Icons.Rounded.Lock,
        contentDescription = stringResource(R.string.cd_security_icon),
        modifier = Modifier.size(64.dp),
        tint = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun HeaderText() {
    Text(
        text = stringResource(R.string.welcome_back),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun SubtitleText() {
    Text(
        text = stringResource(R.string.enter_assessment_credentials),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun PasswordField(
    password: String,
    isError: Boolean,
    passwordVisible: Boolean,
    onPasswordChange: (String) -> Unit,
    onToggleVisibility: () -> Unit,
    onLoginSubmit: () -> Unit
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(stringResource(R.string.password)) },
        singleLine = true,
        isError = isError,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onLoginSubmit() }
        ),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (passwordVisible)
                stringResource(R.string.cd_hide_password)
            else
                stringResource(R.string.cd_show_password)

            IconButton(onClick = onToggleVisibility) {
                Icon(imageVector = image, contentDescription = description)
            }
        },
        supportingText = {
            AnimatedVisibility(visible = isError) {
                Text(stringResource(R.string.error_incorrect_password))
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .testTag(TAG_EDT_LOGIN_PASSWORD)
    )
}

@Composable
private fun LoginButton(
    isLoginEnabled: Boolean,
    isAuthenticating: Boolean,
    onLoginSubmit: () -> Unit
) {
    if(isAuthenticating) {
        ThreeBarLoader(
            modifier = Modifier.height(55.dp)
        )
    } else {
        val coroutineScope = rememberCoroutineScope()
        TextButton(
            onClick = {
                coroutineScope.launch {
                    delay(300) //add a delay to highlight the click animation
                    onLoginSubmit()
                }
            },
            enabled = isLoginEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag(TAG_BTN_LOGIN_SUBMIT),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = stringResource(R.string.login),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun ThreeBarLoader(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "loader_transition")

    val barScales = (0..2).map { index ->
        transition.animateFloat(
            initialValue = 0.4f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(offsetMillis = index * 150)
            ),
            label = "bar_scale_$index"
        ).value
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        barScales.forEach { scale ->
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(24.dp * scale)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}