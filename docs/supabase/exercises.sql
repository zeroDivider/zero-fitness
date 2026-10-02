-- Desired current schema. New projects: run this file.
-- Existing projects: apply docs/supabase/migrations/ (do not re-run
-- CREATE TABLE IF NOT EXISTS to pick up new columns).
--
-- Exercises table (maps to ExerciseEntity).

create table if not exists public.exercises (
  id uuid primary key default gen_random_uuid(),
  name text not null,
  description text,
  name_sr text,
  description_sr text,
  allow_weight boolean not null default true,
  equipment text,
  muscle_groups_csv text, -- e.g. "chest,triceps"
  difficulty text check (difficulty is null or difficulty in ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'ELITE')),
  is_compound boolean not null default true,
  video_url text,
  created_at_epoch_ms bigint not null default (extract(epoch from now()) * 1000)::bigint,
  updated_at_epoch_ms bigint not null default (extract(epoch from now()) * 1000)::bigint
);

create index if not exists exercises_name_idx on public.exercises (name);

create or replace function public.set_exercises_updated_at_epoch_ms()
returns trigger
language plpgsql
as $$
begin
  new.updated_at_epoch_ms = (extract(epoch from now()) * 1000)::bigint;
  return new;
end;
$$;

drop trigger if exists exercises_set_updated_at on public.exercises;

create trigger exercises_set_updated_at
before update on public.exercises
for each row
execute function public.set_exercises_updated_at_epoch_ms();
