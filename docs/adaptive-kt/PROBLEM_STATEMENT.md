# AdaptiveKt — Problem Statement

## Context
Compose Multiplatform is increasingly used to build UI across Android, desktop, web, and WASM. Existing adaptive UI approaches are often either:

- too tied to platform-specific paradigms,
- too low-level for application scaffolding,
- based on CSS-like class strings,
- or not idiomatic with Compose/Modifier/slots.

Developers building multiplatform business apps need a reusable, composable, idiomatic library for adaptive page structure, responsive layouts, navigation, form surfaces, and data presentation.

## Core Problem
Apps today must support very different window sizes and input modes while remaining maintainable and platform-agnostic. Without a shared adaptive toolkit, teams tend to:

- duplicate responsive layout patterns,
- overuse fixed breakpoints or platform-specific branches,
- create brittle custom scaffolding,
- or leak web/mobile conventions into Compose APIs.

## Opportunity
AdaptiveKt should provide a Compose-native adaptive layer for common enterprise UI patterns:

- responsive containers and grids,
- adaptive navigation scaffolds,
- form layouts that reshape across form factors,
- data views that switch between tables and cards,
- action surfaces that move between visible toolbar buttons and mobile overflow/FAB,
- master-detail and dashboard patterns.

## Goal
Design an open source architecture and public API for `AdaptiveKt` that is:

- idiomatic for Compose Multiplatform,
- driven by slots, typed parameters, and design tokens,
- complementary to Material 3 rather than replacing it,
- platform-agnostic across Android, desktop, web, and WASM,
- focused on high-level adaptive patterns, not rigid visual components.

## Constraints

- No dependency on a single app or product concept such as OneChat.
- No visual HTML/CSS class string patterns.
- No rigid opinionated UI skins.
- Maintain compatibility with Compose Multiplatform limitations where needed.
- Prioritize a clean, maintainable public API that can be published as a library.
