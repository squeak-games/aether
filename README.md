# Aether

**Squeak Games' cross-product Android XR platform for the Wyldore ecosystem.**

---

| Field | Detail |
|:---|:---|
| **Project** | Aether |
| **Studio** | Squeak Games Inc. |
| **Status** | Under active development |
| **License** | Apache 2.0 |
| **Target Platforms** | Android XR (Hushwild, Stillgrove) |
| **Repository** | squeak-games/aether |

---

## What Aether Is

Aether is the shared technology layer that powers Squeak Games' Wyldore universe of Android XR experiences. Rather than building two separate spatial products from scratch, Aether provides a common foundation of procedural audio, persistent creature state, and spatial capability abstractions that both titles consume.

Two products currently build on Aether:

- **Wyldore: Hushwild** — an ambient, audio-first wellness companion for Android XR audio and display glasses. On-the-go, passive, always-on.
- **Wyldore: Stillgrove** — a spatial creature-tending experience for XREAL Project Aura. At-home, active, hand-tracked.

Together, they create a continuous cycle of care across the Wyldore narrative. Aether is what makes one shared architecture possible.

## Module Layout

| Module | Purpose | Consumed By |
|:---|:---|:---|
| `:core-audio` | Oboe C++ JNI bridge, oscillator bank, envelope generators, filter chain, crossfade mixer, 6DoF spatial panner. The procedural audio pipeline used for creature vocalizations, ambient soundscapes, and care response feedback. | Hushwild, Stillgrove |
| `:core-data` | Room database base classes, session logging abstractions. Defines the persistent creature, bond graph, and session history schemas used across products. | Hushwild, Stillgrove |
| `:core-model` | Shared Wyldore data models: creature state enums, bond level definitions, environmental context types. The common vocabulary of the Wyldore universe. | Hushwild, Stillgrove |
| `:core-xr` | Utility wrappers for Jetpack XR capability detection (`LocalSpatialCapabilities`), projected activity lifecycle helpers, touchpad input abstractions. | Hushwild, Stillgrove |

## Design Principles

| Principle | Description |
|:---|:---|
| **Shared foundation, distinct products.** | Aether is the infrastructure. Hushwild and Stillgrove are the experiences. The platform is never the product. |
| **Privacy by construction.** | No network clients anywhere in the dependency graph. Microphone, camera, IMU, and location data are processed on device and discarded. Privacy is not a policy on top of Aether, it is a property of Aether. |
| **Hardware-aware, not hardware-bound.** | Aether queries `LocalSpatialCapabilities` at runtime and degrades gracefully when features are missing. Products ship on more devices than they were designed for. |
| **Honest about scope.** | Aether is open-sourced as architectural evidence of competence, not as a shipping product. The repo intentionally reflects its current stage. |

## Technology Stack

| Layer | Technology |
|:---|:---|
| **Language** | Kotlin (application logic, state, AI), C++ (audio engine via JNI) |
| **Build** | Gradle with Kotlin DSL, version catalogs (`gradle/libs.versions.toml`) |
| **XR Framework** | Jetpack XR SDK (SceneCore, ARCore for Jetpack XR, Compose Glimmer) |
| **Audio** | Android Oboe (C++) with low-latency AAudio and OpenSL ES fallback |
| **Persistence** | Room (SQLite), local-only |
| **DI** | Hilt (Dagger) |
| **Architecture** | MVVM + Clean Architecture with injectable boundaries |
| **Min Android SDK** | Aligned with Android XR requirements |

## Build

```bash
./gradlew :core-audio:assemble
./gradlew :core-data:assemble
./gradlew :core-model:assemble
./gradlew :core-xr:assemble
./gradlew test
```

The audio module requires the Android NDK to compile the C++ synthesis layer. Other modules are pure Kotlin and build on the JVM.

## Privacy

Aether has no network client. There is no HTTP stack, no analytics SDK, no cloud sync layer. This is enforced by construction: the dependency graph contains no remote I/O libraries. Privacy is a property of the architecture, not a promise made in a policy document.

See `docs/PRIVACY.md` for the full model.

## Documentation

- [`docs/PRD.md`](docs/PRD.md) — Product Requirements Document for Aether
- [`docs/PRIVACY.md`](docs/PRIVACY.md) — Privacy architecture
- [`docs/CATALYST.md`](docs/CATALYST.md) — Catalyst submission traceability (deck claim to file mapping)

## License

Apache 2.0. See [LICENSE](LICENSE).

## Status

Under active development. Aether is open-sourced in good faith as part of the Android XR Developer Catalyst Program submission. The repos that consume Aether (`wyldore-hushwild`, `wyldore-stillgrove`) are the product surfaces. Aether is the platform they stand on.

---

*Squeak Games Inc.*