# v0.0.2-alpha — Modular architecture release

## Architecture

- Migrated from single-module app to 12 modules: `:core:` (domain, common, datastore, network, data, designsystem, navigation) + `:feature:` (auth, home, weather, profile, location, currency_rates)
- `build-logic/`: 5 convention plugins (`vbbs.android.*`, `vbbs.kotlin.jvm`); Hilt and desugaring folded in — one source of truth for module config
- Features self-register navigation entries via Hilt `@IntoSet` multibinding; `:app` has zero feature-screen imports (grep-verified)
- Dependency direction enforced: features -> `core:data` -> `core:network`/`core:datastore` -> `core:domain`; `core:domain` is pure Kotlin

## New: CurrencyRates feature

- Daily CBR rates via own xml->json converter (`cbr/daily`, `cbr/dynamic`)
- Date selection (DatePickerDialog) for historical daily rates
- DateRangePicker interval selection for dynamics
- Currency dynamics sub-screen: 30-day history, min/max/avg stats
- Typed errors (`AppError`: Network/Server/Unknown), classified at data boundary, localized en/ru, Retry flow
- Pull-to-refresh, effective-date title in top bar
- Verified against live api via Hilt instrumented tests

## New: TopBar system

- Shell renders hoisted state; features declare title/actions per entry
- SideEffect re-assertion + owner-guard against lifecycle races
- Navigation icon: back for sub-screens, menu for top-level

## Weather improvements

- Two-flag state machine (isLoading/isRefreshing), retry, pull-to-refresh
- Reactive reload on location change

## Fixes

- Home: first-frame real data (hot StateFlow + seeded stateIn, `---` gone)
- Splash gate corrected for hot flows (filterNotNull, session-guarded)
- Logout events via Channel (rotation-safe, no conflation)
- ViewModel init-order crash fixed (state before init; Main.immediate law)
- androidTest APK INTERNET permission (silent socket-timeout cause)

## Deferred / known issues

- SOCKS proxy 127.0.0.1:9150 hardcoded in NetworkModule (Tor) — breaks without it; needs build flag
- Token encryption: CryptoManager AES-GCM/Keystore in place; Tink variant removed as dead
- Placeholder screens (blogs/shops/chats) await feature graduation
- Weather conditions not localized yet (provider strings)

## Engineering laws

1. One source of truth: derived `stateIn` seeds from `upstream.value`, never a hardcoded default.
2. Repositories expose existing state as hot StateFlow (appScope, Eagerly); cold flows for one-shots only.
3. `e.message` is for `Log.e`, never for `Text()`. Typed `AppError` at the boundary, localized strings at render.
4. Events ride Channels; state rides StateFlow.
5. Screens take callbacks, not ViewModels; entries take navigators, not backstacks.
6. The shell holds no data layer (grep-enforced). Exception: splash-gate in MainActivity.
7. `mkdir -p` before `git mv`.
8. `**/build/` in .gitignore — one rule for all modules.
9. Class FQNs are global across the APK — module-prefix your classes.
10. The working source is the spec. Verify library behavior on your version.
11. One commit per step; build green before commit; CLI over IDE.
12. Second occurrence of a pattern = extract shared code.
13. Nav3: key fields live in the key object; pass `entry -> screen -> VM` explicitly (SavedStateHandle is not auto-populated — crash-verified).
14. androidTest APK is its own app: inherits nothing, permissions included.
15. Ktor renames exceptions: classify with `io.ktor.client.network.sockets.*`, never `java.net.*`.
16. Property initializers run top-to-bottom: `init { load() }` requires state declared above.
    