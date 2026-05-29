# AdaptiveAvatar

Purpose: user avatar with initials fallback and shape selection.

Use it for people, accounts, and owners.

Primary API: `AdaptiveAvatar(name, modifier, size, shape, image)`.

Simple example:

```kotlin
AdaptiveAvatar(name = "Alicia Romero")
```

Advanced example: pass an image slot from platform-specific image loading code.

Responsive notes: choose smaller sizes in dense tables.

Multiplatform notes: image loading is caller-provided; Kamel is demo JVM-only.

Limitations: no built-in remote image loader.
