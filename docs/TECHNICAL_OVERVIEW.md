# Technical Overview

This document is for engineers reviewing the project. The main `README.md` explains the product and portfolio context; this file focuses on how the app is put together.

## Application Shape

`zero-fitness` is a single-module Android app:

```text
app/
  src/main/java/com/zero/fitness/
```

The current strongest feature area is the exercise library. Workout editor, preview, and session screens exist as early UI shells, but the product logic for those areas is still planned.

## Layering

The app follows a simple layered structure:

```text
Compose screen
  -> ViewModel
    -> domain repository contract
      -> repository implementation
        -> Room DAO
        -> Supabase remote data source
```

The intent is to keep UI state and navigation concerns in the UI layer, business/data decisions in repositories, and persistence details behind interfaces.

The local-first approach with a pending-sync flag was chosen deliberately: it means the app stays usable without a network, changes are never lost silently, and the sync behavior is explicit and testable rather than hidden inside a library. It also keeps the repository the single source of truth regardless of whether the remote is reachable.

## Key Areas

| Area | Path | Purpose |
|---|---|---|
| App entry/navigation | `app/src/main/java/com/zero/fitness/AppRoot.kt` | Sets up typed navigation between screens. |
| Exercise UI | `app/src/main/java/com/zero/fitness/ui/screens/workout/exercise/` | Exercise editor, details, view models, and navigation. |
| Library UI | `app/src/main/java/com/zero/fitness/ui/screens/library/` | Search, filter, list, and sync entry point for exercises. |
| Domain models | `app/src/main/java/com/zero/fitness/domain/model/` | Exercise/workout models and shared enums. |
| Repository contract | `app/src/main/java/com/zero/fitness/domain/repository/ExerciseRepository.kt` | Interface used by higher layers. |
| Repository implementation | `app/src/main/java/com/zero/fitness/data/repository/ExerciseRepositoryImpl.kt` | Coordinates local data and remote sync. |
| Local database | `app/src/main/java/com/zero/fitness/data/local/db/` | Room database, DAOs, entities, converters, migrations. |
| Remote data | `app/src/main/java/com/zero/fitness/data/remote/` | Supabase DTOs and remote data source. |
| Dependency injection | `app/src/main/java/com/zero/fitness/di/` | Hilt modules for data and repository wiring. |

## Data and Sync

Exercises are stored locally in Room and can be synchronized with Supabase.

The repository observes local data first, then syncs when the configured `SyncPolicy` allows it. Locally changed records are marked as pending. During sync, pending changes are pushed, remote data is fetched, and locally synced records that no longer exist remotely can be removed.

The Supabase schema is documented in `docs/supabase/exercises.sql`. Existing projects should use one-shot migration files under `docs/supabase/migrations/` instead of rerunning the desired schema file.

## UI Conventions

The UI is built with Jetpack Compose and Material 3.

Project-specific conventions live in `.cursor/rules/`. These are instruction files read by the Cursor AI assistant to enforce project patterns during development — similar to a linting ruleset, but written in plain language for the AI rather than code.

- `.cursor/rules/zero-fitness-compose.mdc`
- `.cursor/rules/zero-fitness-screen-naming.mdc`
- `.cursor/rules/zero-fitness-strings.mdc`

Important conventions:

- Screens are named entity-first, for example `ExerciseEditorScreen`.
- Feature screens live under `ui/screens/<feature>/`.
- Shared UI lives under `ui/screens/common/`.
- User-facing strings go through Android string resources.
- Existing locales must receive matching string keys when new copy is added.

## Testing

The current test suite includes repository, view model, result-handling, and UUID tests under:

```text
app/src/test/java/com/zero/fitness/
```

Run:

```bash
./gradlew testDebugUnitTest
```

The current tests cover the repository sync logic, view model state, result/error handling, and UUID generation. Integration tests and UI tests are not yet in place — those are planned as the workout features take shape and the data layer stabilizes.
