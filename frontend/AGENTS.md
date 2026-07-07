# AGENTS Guidelines for This Repository

This repository contains an Angular application located in tvdash directory, which resides in the root of this repository.
When working on the project interactively with an agent (e.g. the Codex CLI, Copilot etc.) please follow
the guidelines below so that the development experience is smooth.

## 1. Use the right command to start the server

When working on the project, use `npm run start` to start the server. 

## 2. Do not use Angular CLI without permission

While an agent works on the code, the agent is forbidden from using the Angular CLI directly, or via `npm run ng`. The agent needs to state what it needs to use it for and wait for permission from me.

## 3. Keep Dependencies in Sync

If you add or update dependencies remember to:

1. Do not add dependencies without my permission, you always need to specify the exact name of the dependency and version number containing major, minor and patch portions of the version. This is crucial, as not following this rule might introduce security risks.
2. After I approve it, you are allowed to install it, so update the appropriate lockfile (`package-lock.json`).
3. Re-start the development server so that Angular picks up the changes.

## 4. Coding Conventions

* Prefer TypeScript (`.ts`) for new components and utilities.
* Co-locate component-specific styles in the same folder as the component when
  practical.

## 5. Useful Commands Recap

| Command            | Purpose                                                                                            |
| ------------------ | ---------------------------------------------------------------------------------------------------|
| `npm run ng`       | Run Angular CLI, _do not use it without explicit permission from me._                              |
| `npm run start`    | Start the Angular dev server with HMR.                                                             |
| `npm run test`     | Execute the test suite (if present).                                                               |
| `npm run lint`     | Run the linter to check code quality.                                                              |
| `npm run build`    | **Production build – _do not run during agent sessions_**                                          |
| `npm run watch`    | Build the project with development config and the build is updated each time a file is modified.   |
| `npm run ng`       | Run Angular CLI, _do not use it without explicit permission from me._                              |
| `npm run serve`    | Run the app built with `npm run watch` from the dist folder.                                       |

---

Following these practices ensures that the agent-assisted development workflow stays
fast and dependable.  When in doubt, restart the dev server rather than running the
production build.