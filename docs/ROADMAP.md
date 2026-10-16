# Roadmap

This roadmap describes the product direction and current progress. It is not a promise of exact dates; it is here so someone opening the repo can understand what is done, what is next, and why the work is ordered this way.

## Guiding Goals

- Build a useful fitness app in small, reviewable steps.
- Use the exercise library as the base for every workout feature.
- Prefer clear local-first behavior with explicit sync states.
- Keep documentation readable enough that a reviewer can understand the project without a walkthrough.
- Improve localization along the way instead of treating it as a separate milestone.

## Milestones

| Milestone | Status | Goal |
|---|---|---|
| Exercise Library | Done | First complete product slice: create, edit, search, filter, view, and sync exercises. |
| Workout Planning | Planned | Let users build reusable workout plans from the exercise library. |
| Workout Sessions and History | Planned | Let users run a planned workout, track sets, reps, weight, and keep a usable history of completed sessions. |
| Reporting | Planned | Add useful summaries after workout sessions, then weekly reporting once enough history exists. |
| Supabase Auth | Planned | Add accounts so training data can belong to a real user, sync safely across devices, and support future personalized features. |
| Kotlin Multiplatform and iOS UI | Planned | Move suitable domain/data code toward KMP and add an iOS UI after Supabase Auth is in place. |

## Version 2 Ideas

These are not part of the immediate build. They are parked here so the direction is visible without mixing them into the first product pass.

| Idea | Status | Goal |
|---|---|---|
| Exercise Import, Model, and UI Polish | Planned | Import existing exercise data, then adjust models and UI around the imported structure instead of polishing them separately too early. |
| Pagination and Loading | Planned | Handle larger exercise lists and optimize data loading. |
| Premium Features | Planned | Explore paid capabilities once the free core is useful. |

