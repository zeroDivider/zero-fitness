# zero-fitness

## Supabase configuration

Supabase URL and key are not stored in source control.

### Local development

Add these entries to the gitignored `local.properties` at the project root (keep existing `sdk.dir`):

- `supabase.url`
- `supabase.key`

### GitHub Actions

Add repository secrets under **Settings → Secrets and variables → Actions**:

- `SUPABASE_URL` — same value as `supabase.url`
- `SUPABASE_KEY` — same value as `supabase.key`

Use the Supabase publishable (anon) key, not the service role key.

### Schema

SQL applied in Supabase lives in `docs/supabase/`. The `exercises` table (maps to `ExerciseEntity`) is in `docs/supabase/exercises.sql` (desired current schema for new projects). If the table already exists, apply one-shot changes from `docs/supabase/migrations/` in the SQL editor instead of re-running the create file.
