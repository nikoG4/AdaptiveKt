# AI Workspace Route Validation

## Automated Tests
`AiRouteCodecTest` runs as part of the `desktopTest` suite in the AI Workspace Demo. It serializes and deserializes the state machine of the `AdaptiveNavigator`.
The following routes are explicitly tested for serialization consistency:
- `/` (DashboardScreen)
- `/chats` and `/chats/{id}`
- `/prompts` and `/prompts/{id}`
- `/assistants` and `/assistants/{id}`
- `/playground`
- `/knowledge` and `/knowledge/{id}`
- `/tools` and `/tools/{id}`
- `/evaluations` and `/evaluations/{id}`
- `/settings`

## Smoke Tests
Because the frontend is entirely mock-driven and compiles completely to Wasm with no dynamic network dependency on a backend, full functional testing is handled via automated component compilation tests (`compileKotlinWasmJs`). Since the application uses strong type-safety for its UI screens mapping to `AiRoute` sealed class hierarchies, passing compilation ensures no routes are left orphaned or inaccessible.
