# Beta01 keyboard, focus and accessibility audit

This audit describes the interaction-hardening changes stacked on top of
`feat/beta01-visual-foundations`. **Code added != runtime validation passed.**
Keep this PR unmerged until its checks and manual keyboard matrix are reviewed.

## Controls and behavior

| Component | Keyboard | Focus | Semantics | Status |
| --- | --- | --- | --- | --- |
| Select trigger | Enter, Space, Down open | Visible token-driven focus border | Button, expanded/collapsed and label | Source implemented; manual test pending |
| Select popup | Up/Down/Home/End traverse enabled visible options; Enter selects; Escape closes | Popup focus target or search input, depending on mode | Options expose selected + radio role | Source implemented; manual test pending |
| MultiSelect popup | Same traversal; Enter toggles without closing; Escape closes | Search input focused on opening | Option rows expose selected + checkbox role | Source implemented; manual test pending |
| Search input in popup | Space types a space; Home/End move caret; Escape closes | Focus requested from inside the popup on mount | Input label and errors | Source implemented; manual test pending |
| Filtering | Local query filters by labels; external callback opts out of local filtering | Highlight is reconciled to visible, enabled options | Disabled option cannot be selected by keyboard | Unit tests added; platform run pending |
| Dialogs | Platform back/Escape dismissal | Platform Dialog semantics | Existing button semantics | Focus trap & restored focus still require manual evidence |

Important implementation details:
- Process **KeyDown only**, never the paired KeyUp. This prevents one key press
  triggering two actions.
- Keyboard commands are normalized in a pure function with tests.
- Searchable popups request focus only once the search input is in the popup
  composition; other popups focus their container to receive navigation.
- Disabled and filtered-out options cannot remain the active highlight after
  the visible option list changes.
- Previous navigation without a highlighted option starts at the last enabled option.
- Option rows explicitly describe selected state and appropriate radio/checkbox roles.
- Theme motion and focus tokens style the select trigger. No arbitrary fixed duration.

## Test plan — actual platforms

Desktop/JVM: use mouse and keyboard to open Select, navigate with arrows,
Home and End, select with Enter, clear, reopen, search and dismiss with Escape.
Repeat with a disabled option and a list replaced while popup is open.

Web/Wasm: repeat on both Chromium and Firefox if possible. Verify TAB order,
focus return to trigger, input typing (particularly Space and Home/End),
and no double-toggles from key release.

Android: verify touch targets, accessibility description and no unnecessary
focus steals when the software keyboard appears. Confirm popups fit compact screens.

iOS: perform app-level verification on macOS + device/simulator. Do not infer
iOS accessibility from Kotlin source-set compilation alone.

## Remaining gaps (not asserted as fixed)

1. Screen reader interaction evidence (TalkBack, VoiceOver, desktop readers).
2. Dialog initial focus, focus trap and restoration on dismiss.
3. Mixed input-method edge cases and IME confirmation behavior.
4. Reduced-motion detection from OS and full focus screenshots.
5. RTL/localization of English stateDescription values.
6. All targets + real browser screenshots must pass prior to beta.

## Development and Actions budget

This branch is stacked on the visual PR and targets its feature branch. It
intentionally does not trigger `main`-targeted CI. When the visual PR is
approved, rebase/retarget this change, run **one** focused test build, and only
then evaluate integrating it into main. Do not duplicate the visual capture run.
