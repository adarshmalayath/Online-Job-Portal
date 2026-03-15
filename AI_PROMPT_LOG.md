# AI Usage Log (Prompt Record)

This document records prompts used with AI during development of this submission.

## Student Submission Context

- Project: `GP7_CO7214GW2_submission`
- Module: CO7214 (as referenced in project files)
- AI tool used: ChatGPT/Codex
- Purpose: Development assistance, code scaffolding, documentation, and refinement

## Prompt Log

| # | Date (2026) | Prompt Used | AI Assistance Provided | Outcome in Project |
|---|---|---|---|---|
| 1 | 12 Mar | "create one frontend and api gateway for this employent service java backend code" | Scaffolded `frontend` and `api-gateway` around existing Spring backend. | Added React frontend + Express gateway structure and run flow. |
| 2 | 12 Mar | *(provided React code block)* | Integrated provided React component into frontend setup. | Replaced frontend entry/component files with provided implementation. |
| 3 | 12 Mar | "how to run" | Generated execution steps for backend, gateway, frontend. | Added/updated run instructions and verified startup commands. |
| 4 | 12 Mar | "where is the register, login, job dashboard pages" | Identified missing routes/pages and backend limitations. | Clarified gap and prepared route implementation plan. |
| 5 | 12 Mar | "add it also update profile" | Implemented `/register`, `/login`, `/dashboard`, `/profile` and profile update flow. | Added React routes/pages, auth guard, profile load/update integration. |
| 6 | 12 Mar | *(screenshot showing Not Found on register)* | Diagnosed gateway routing issue and fixed proxy path behavior. | Fixed `/api` forwarding and request body handling in gateway. |
| 7 | 12 Mar | "change the colors to back and white with gray" | Updated theme to black/white/gray palette. | Modified frontend CSS tokens and component colors. |
| 8 | 12 Mar | "remove gradient and add gray borders" | Removed gradients and applied gray borders globally. | Updated UI styling rules in frontend CSS. |
| 9 | 12 Mar | "in profile show email name and user id as un editable field" | Added account info fields in profile page as non-editable inputs. | Updated Profile UI and styles. |
|10 | 12 Mar | "don't mention read only" | Removed explicit "read only" wording in UI text. | Updated label text while keeping fields non-editable. |
|11 | 12 Mar | "add the respective links to reccommended courses" | Added provider-based clickable course links (Udemy/Coursera/etc). | Updated dashboard course rendering + link helper. |
|12 | 12 Mar | "create on github url to access this from public" | Initialized git repo, committed code, created public GitHub repo, pushed. | Published repository: `adarshmalayath/GP7_CO7214GW2_submission`. |
|13 | 13 Mar | "create a readme file" | Wrote consolidated setup/use README. | Updated root `README.md`. |
|14 | 13 Mar | "add everything" | Staged, committed, and pushed pending changes. | Synced repository to `main`. |
|15 | 14 Mar | "create a document which explains the code" | Authored architecture and flow explanation document. | Added `CODE_EXPLANATION.md` and pushed. |
|16 | 14 Mar | "it is empty" | Verified file content, then committed/pushed missing file. | `CODE_EXPLANATION.md` visible in GitHub. |
|17 | 15 Mar | "remove commented lines from the files" | Removed code comments from targeted files and validated builds. | Updated multiple backend/source files. |
|18 | 15 Mar | "from all the files" | Expanded cleanup scope across repo file types and revalidated. | Removed additional comments (Java/config/XML). |
|19 | 15 Mar | "i need an AI log which consists of prompts for submitting to professor, for which i have used AI" | Generated this prompt log document for submission transparency. | Added `AI_PROMPT_LOG.md`. |

## How AI Output Was Used

- AI outputs were used for scaffolding, refactoring, bug fixing, and writing documentation.
- Commands/build checks were executed in the local environment.
- Generated code and config were reviewed and adjusted during iteration.

## Verification Performed

- Backend compile checks (`mvn -DskipTests compile`)
- Frontend production build checks (`npm run build`)
- Gateway syntax/startup checks (`node --check server.js`, health endpoint)

## Declaration

This log reflects prompts used during development of this submission and the corresponding AI-assisted tasks.
