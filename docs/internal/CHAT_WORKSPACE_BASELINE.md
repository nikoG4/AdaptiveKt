# Chat Workspace Baseline

Date: 2026-06-17

## Current State

* **Branch:** `feat/communication-suite-demo`
* **Head Commit:** `34bd192 fix(validation): handle canonical communication routes`
* **Latest Green CI Run:** Run ID 27662853330 (completed success for `feat(showcase): implement communication suite demo`)

## Existing Routes

The current demo (Communication Suite) has the following primary routes based on previous validation reports:

* `/`
* `#/chat`
* `#/chat/inbox`
* `#/chat/conversation/c_1`
* `#/chat/conversation/c_2`
* `#/chat/search`
* `#/mail`
* `#/mail/inbox`
* `#/mail/thread/t_1`
* `#/mail/thread/t_2`
* `#/mail/compose`
* `#/settings`

## Existing Feature Inventory

* **Chat:** Basic inbox, basic conversation detail, search placeholder.
* **Mail:** Inbox, thread reading pane, compose mock, settings.
* **Settings:** Basic shared settings screen.

## Known Visual and Architectural Problems

* **Visual Disconnection:** Mail and Chat feel like two separate apps jammed together under "CommSuite" / "AdaptiveKt Admin". 
* **Theming Issues:** Dark mode is essentially light mode in many surfaces, rendering it unusable or not representative of true dark theming.
* **Navigation Placement:** Bottom navigation logic may be rigid or ignoring breakpoints correctly.
* **Layout Use:** Missing proper details pane on expanded/desktop views.
* **Icons:** Placeholder icons and text/Unicode glyphs are used instead of a robust icon set.
* **Mock Data:** Data is shallow and random (`Generated Conversation 1`, etc.), not representing a realistic product.

## Files Implementing Mail

We will identify and remove the Mail implementation. Key locations likely include:

* `examples/communication-suite-demo/src/commonMain/kotlin/io/github/adaptivekt/examples/communication/mail/`
* Route configurations referencing `mail`
* Mock data associated with mail threads.
