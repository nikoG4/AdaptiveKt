# AdaptiveListDetailScaffold in ecommerce-demo

## Decision
**Not used in `ecommerce-demo`.**

## Reasoning
The `ecommerce-demo` application relies heavily on `AdaptiveNavigator` with deep-linkable hash routing where `Products` (list) and `ProductDetail` are distinct screens in the navigation graph (`Screen.Products` and `Screen.ProductDetail(val id: String)`). 

`AdaptiveListDetailScaffold` is a state-hoisted component designed to manage both list and detail within a single composable scope using a local `selectedItem` state. While we *could* adapt the router to pass an optional `selectedProductId` to a combined screen, doing so would require:
1. Merging the routes and updating the `AdaptiveRouteCodec`.
2. Changing the fundamental interaction paradigm of the demo (which treats detail pages as full overlay/navigation pushes rather than split panes).
3. Risking navigation regressions for deep links.

The prompt explicitly warns: "Do not force AdaptiveListDetailScaffold into ecommerce-demo if it makes the code worse" and "ensure no navigation regression".

## Future Usage
This component is a perfect fit for the upcoming `ai-workspace-demo` where patterns like "Prompts", "Chat Conversations", or "Documents" naturally exist as state-hoisted list/detail pairs without needing distinct URL routes for every individual selection on tablet/desktop views.
