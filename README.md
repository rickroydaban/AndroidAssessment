# Android Practical Assessment

![UI Tests](https://github.com/rickroydaban/AndroidAssessment/actions/workflows/android.yml/badge.svg)

A production-ready Android application built entirely with Jetpack Compose, designed to fulfill the practical assessment requirements while demonstrating senior-level architectural patterns, UI/UX polish, and robust automated testing.

## 🏗 Architecture & Tech Stack

This project was built with a focus on **Separation of Concerns**, **Testability**, and **Modern Native Android Development**.

* **UI Framework:** 100% Jetpack Compose (Material 3).
* **State Management:** Strict State Hoisting. UI components are purely declarative and stateless, making them highly predictable and easy to test via semantic trees.
* **Routing:** Leveraging the modern Navigation 3 `NavDisplay` for direct, granular control over backstack state and transition animations.
* **Automated Testing:** Jetpack Compose UI Test framework (`createAndroidComposeRule`). Tests are optimized to query the Compose Semantic Tree directly in memory (`onAllNodesWithText`), bypassing slow IPC framework bottlenecks.
* **CI/CD Pipeline:** Integrated GitHub Actions workflow using macOS runners to execute instrumentation tests with hardware acceleration (HAXM) on every push.

## ✓ Assessment Requirements Mapping

| Requirement | Implementation Details |
| :--- | :--- |
| **Page 1: Login** | Implemented with `OutlinedTextField`. Includes robust state hoisting, custom haptic feedback modifiers on click, and an animated simulated authentication state. Validates against `Test@2026`. |
| **Page 2: Conditional Check** | Displays target string inside a Material 3 `Surface`. Next/Exit buttons mapped to specific test tags. Instrumentation test queries the semantic node tree to evaluate the existence of "test text 1" dynamically. |
| **Page 3: PIN Input** | Utilizes `KeyboardType.NumberPassword` to strictly enforce the OS-level numeric dialpad. Input is deeply sanitized to allow a maximum of 4 digits, validating against `8526`. |
| **Instrumentation Test** | A single, comprehensive E2E test (`PracticalAssessmentTest.kt`) that boots the app, navigates the authentication flow, evaluates the Page 2 conditional, and inputs the final PIN. |

## 🚀 How to Run & Test

### Option 1: View the CI/CD Pipeline (Recommended)
This repository is wired to a GitHub Action that automatically spins up an Android emulator and executes the instrumentation test suite. 
* Click the **Actions** tab at the top of this repository.
* Select the latest **Android CI UI Tests** workflow to view the live execution logs and passing test results.

### Option 2: Local Execution via Terminal
If you have an Android emulator or physical device connected via ADB, you can run the test suite locally without opening Android Studio.

```bash
# Clone the repository
git clone [https://github.com/YOUR_GITHUB_USERNAME/YOUR_REPO_NAME.git](https://github.com/YOUR_GITHUB_USERNAME/YOUR_REPO_NAME.git)
cd YOUR_REPO_NAME

# Execute the instrumentation tests
./gradlew connectedAndroidTest
