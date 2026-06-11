# Android Back Navigation Manual Validation Plan

## What Cannot Be Fully Verified in CI
The underlying behavior of `androidx.activity.compose.BackHandler` interacting with the system `OnBackPressedDispatcher` requires an Android environment with a real activity stack. While common logic tests verify the backstack state changes, the actual interception of the hardware/gesture back event and the OS-level "finish activity" behavior must be validated manually on an emulator or device.

## What Can Be Verified by Unit Tests
- `canGoBack` is false on the initial route.
- `navigate` pushes a route and sets `canGoBack` to true.
- `goBack` successfully pops the route stack to return to the previous state.
- At the root route, `goBack` has no effect.
- `replace` modifies the current top of the stack without growing the backstack.

## What Must Be Manually Checked on Device/Emulator

### AI Workspace Android
1. Launch the app.
2. Navigate Home → Chats → Prompt Library → Settings.
3. Press Android system Back.
   - Expected: Navigates from Settings → Prompt Library.
4. Press Back again.
   - Expected: Navigates from Prompt Library → Chats.
5. Press Back until the root is reached.
6. At the root, press Android Back.
   - Expected: The `BackHandler` becomes disabled (since `canGoBack` is false), allowing the system to handle the back press (the app will exit/close).

### Ecommerce Android
1. Launch the app.
2. Navigate Home → Shop → Cart → Checkout.
3. Press Android system Back.
   - Expected: Navigates from Checkout → Cart → Shop → Home.
4. At Home, press Android Back.
   - Expected: The activity finishes / the app closes natively.
