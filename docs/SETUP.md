# Setup

This guide explains how to run `zero-fitness` locally and in CI.

## Requirements

- Android Studio
- JDK 17
- Android SDK 35
- Android emulator or physical Android device
- Supabase project for remote exercise sync

The app targets Android SDK 35 and uses Java 17.

## Local Configuration

Supabase URL and key are not stored in source control.

Add these entries to the gitignored `local.properties` file at the project root. Keep the existing `sdk.dir` entry if Android Studio created one.

```properties
supabase.url=YOUR_SUPABASE_URL
supabase.key=YOUR_SUPABASE_ANON_KEY
```

Use the Supabase publishable/anon key, not the service role key.

## Supabase Schema

SQL files live in `docs/supabase/`.

For a new project, run:

```text
docs/supabase/exercises.sql
```

For an existing project, apply the one-shot migrations under:

```text
docs/supabase/migrations/
```

Do not rerun the desired schema file on an existing project to pick up new columns. Use migrations instead.

## Build and Run

Open the project in Android Studio, confirm `local.properties` contains your Supabase values, select an emulator or device, and run the `app` configuration.

Alternatively, from the command line:

```bash
# macOS/Linux
./gradlew assembleDebug
./gradlew testDebugUnitTest

# Windows PowerShell
.\gradlew assembleDebug
.\gradlew testDebugUnitTest
```

## GitHub Actions

The Android build workflow runs on push and pull request. It:

- checks out the repository
- installs JDK 17
- installs Android SDK packages
- writes Supabase credentials into `local.properties`
- runs unit tests
- assembles a debug APK

Add these repository secrets under GitHub Actions settings:

- `SUPABASE_URL`
- `SUPABASE_KEY`
