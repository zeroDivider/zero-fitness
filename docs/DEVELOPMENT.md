# How I Work

Most AI-generated code I've seen is hard to own. This is my attempt at a different approach.

---

**The boundary is clear:** architectural decisions, data model design, feature planning, and code review stay with me. AI handles code generation — it executes what I have already thought through, not the other way around.

**The process in practice:**

1. Write a ticket first — product decisions and constraints, before touching any tooling
2. Plan the implementation with a capable model, review it, adjust where something feels off
3. Execute in ordered steps with a cheaper model
4. Review and commit each step before moving to the next

A real example: [issue #20](https://github.com/zeroDivider/zero-fitness/issues/20) — six dependent tickets, fully planned with acceptance criteria and ordering rationale, before a single line of code was written.

---

**I also match the model to the task.** Exploring a design decision or drafting a ticket benefits from a stronger model. Writing a DAO or filling in a well-defined function does not. That distinction keeps the feedback loop fast without unnecessary cost.
