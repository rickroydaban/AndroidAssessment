package rickaban.projects.androidassessment

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import rickaban.projects.androidassessment.login.REQUIRED_PASSWORD
import rickaban.projects.androidassessment.login.TAG_BTN_LOGIN_SUBMIT
import rickaban.projects.androidassessment.login.TAG_EDT_LOGIN_PASSWORD
import rickaban.projects.androidassessment.pagetwo.TAG_BTN_NOT_TEST_TEXT
import rickaban.projects.androidassessment.pagetwo.TAG_BTN_PAGE2_TEST_TEXT
import rickaban.projects.androidassessment.pagetwo.TEST_TEXT1
import rickaban.projects.androidassessment.pininput.TAG_EDT_PIN_INPUT

@RunWith(AndroidJUnit4::class)
class PracticalAssessmentTest {

    // 1. MUST BE ABLE TO START THE APP
    // This rule automatically launches MainActivity and provides the Compose testing hooks.
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun executeAssessmentFlow() {
        // ==========================================
        // PAGE 1: Login
        // ==========================================
        // "Must be able to key in the password on the login page and press login button"
        composeTestRule.onNodeWithTag(TAG_EDT_LOGIN_PASSWORD)
            .performTextInput(REQUIRED_PASSWORD)

        composeTestRule.onNodeWithTag(TAG_BTN_LOGIN_SUBMIT)
            .performClick()

        // Sync the framework to ensure Page 2 has completely rendered before checking
        composeTestRule.waitUntil(timeoutMillis = 15000) {
            composeTestRule
                .onAllNodesWithTag(TAG_BTN_PAGE2_TEST_TEXT)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // ==========================================
        // PAGE 2: The Conditional Trap
        // ==========================================
        // "check if 'Test text 1' exists, if exists click... Else click..."

        val targetTextExists = composeTestRule
            .onAllNodesWithText(TEST_TEXT1, substring = true, ignoreCase = true)
            .fetchSemanticsNodes()
            .isNotEmpty()

        if (targetTextExists) {
            // IF EXISTS: click “Test text” button -> Next page
            composeTestRule.onNodeWithTag(TAG_BTN_PAGE2_TEST_TEXT).performClick()

            // Wait for the navigation to Page 3 to finish
            composeTestRule.waitUntil(timeoutMillis = 2000) {
                composeTestRule
                    .onAllNodesWithTag(TAG_EDT_PIN_INPUT)
                    .fetchSemanticsNodes()
                    .isNotEmpty()
            }

            // ==========================================
            // PAGE 3: The PIN Input
            // ==========================================
            // "On the 3rd page, able to input the pin '8526'"
            composeTestRule.onNodeWithTag(TAG_EDT_PIN_INPUT)
                .performTextInput("8526")

        } else {
            // ELSE: click “Not test text” -> Exit the app
            composeTestRule.onNodeWithTag(TAG_BTN_NOT_TEST_TEXT).performClick()
        }
    }
}