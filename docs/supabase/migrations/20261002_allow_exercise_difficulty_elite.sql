alter table public.exercises drop constraint if exists exercises_difficulty_check;
alter table public.exercises add constraint exercises_difficulty_check
  check (difficulty is null or difficulty in ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'ELITE'));
