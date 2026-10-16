# Beta01 release runbook (do not execute automatically)

**Target:** 0.2.0-beta01. **Current published release:** 0.1.0-alpha01.
No release should be issued from the feature branch. Do not trigger
publish-release.yml until a reviewed, approved release commit exists.

1. Review the public API compatibility proposal and migrate any breakages.
2. Complete the visual/interaction QA matrix including focus and reduced motion.
3. Confirm JVM, Android, Wasm and intended iOS platform support on clean consumers.
4. Check KDoc, examples, docs site navigation and published installation steps.
5. Update version and CHANGELOG only on the approved release preparation PR.
6. Run one clean CI plus manual publishing dry-run; resolve any failures before retry.
7. Verify generated signatures, checksums, POMs and published module coordinates.
8. Upload via the guarded Central Portal workflow with explicit authorization.
9. Confirm successful publication before tagging, publishing release notes, or
   changing the README version from alpha to beta.

## Quota-aware CI policy

- Draft PRs run the library build/test path; AI Workspace, Communication Suite
  and Windows visual capture jobs are skipped for draft PRs.
- Ready-for-review PRs run the full selected-path suite, including visual
  capture when UI code has changed.
- Main branch pushes continue running the appropriate full suite.
- Avoid manual workflow dispatches and arbitrary re-runs. Diagnose failures first.

## Failure triage

If SDK setup fails before Gradle, treat it as CI infrastructure, not a library
compile failure. If compilation fails, identify the actual source-set/platform
and fix the first root-cause error before launching another attempt.
