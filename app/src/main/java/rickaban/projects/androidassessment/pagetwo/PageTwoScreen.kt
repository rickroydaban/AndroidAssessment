package rickaban.projects.androidassessment.pagetwo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import rickaban.projects.androidassessment.R

const val TAG_TXT_PAGE2_TEST_TEXT1 = "txt_test_text_1"
const val TAG_BTN_PAGE2_TEST_TEXT = "btn_test_text"
const val TAG_BTN_NOT_TEST_TEXT = "btn_not_test_text"
const val TEST_TEXT1 = "test text 1"

/**
 * PURE STATELESS UI
 * Since there is no user input or dynamic state here, the entire screen
 * is just a pure function of its arguments.
 */
@Composable
fun PageTwoScreen(
    onNextPage: () -> Unit,
    onExitApp: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = TEST_TEXT1,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(24.dp)
                    .testTag(TAG_TXT_PAGE2_TEST_TEXT1)
            )
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    delay(300)
                    onNextPage()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag(TAG_BTN_PAGE2_TEST_TEXT),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = stringResource(R.string.test_text),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = {
                coroutineScope.launch {
                    delay(300)
                    onExitApp()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag(TAG_BTN_NOT_TEST_TEXT),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = stringResource(R.string.not_test_text),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}