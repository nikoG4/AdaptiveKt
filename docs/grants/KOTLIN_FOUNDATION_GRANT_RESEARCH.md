# Kotlin Foundation Grant Research

Research date: 2026-06-04

Project: AdaptiveKt  
Repository: https://github.com/nikoG4/AdaptiveKt  
Published version: `0.1.0-alpha01`  
Maven group: `io.github.nikog4.adaptivekt`

## Official Sources Reviewed

- Kotlin Foundation Grants Program: https://kotlinfoundation.org/grants/
- Kotlin Foundation Grant Agreement / Terms reference: https://kotlinfoundation.org/kotlin-foundation-grant-agreement.pdf
- Kotlin library authors guide: https://kotl.in/api-guide
- Kotlin coding conventions: https://kotlinlang.org/docs/coding-conventions.html
- Kotlin brand usage guidelines: https://kotlinfoundation.org/guidelines/
- Kotlin Foundation language committee guidelines: https://kotlinfoundation.org/language-committee-guidelines/
- Kotlin Foundation guide to incompatible changes: https://kotlinfoundation.org/submitting-incompatible-changes/
- Kotlin Slack code of conduct and guidelines: https://kotlinlang.org/docs/slack-code-of-conduct.html
- Kotlin Foundation 2025 grants announcement, used only as context for recurring expectations: https://kotlinfoundation.org/news/kotlin-foundation-grant-program-2025/

## Current Program Summary

The current Kotlin Foundation grants page states that the program supports developers creating and maintaining open-source libraries, tools, and frameworks that enhance the Kotlin ecosystem and support its sustainable development.

The visible selection criteria emphasize:

1. Addressing a common problem in the Kotlin ecosystem, with a bias toward multiplatform solutions.
2. Responsiveness to security issues, bug reports, Kotlin updates, and library updates.
3. Alignment with library development best practices.
4. A clear strategy for improving the project.

The current grants page links to an application form and says the first round of submissions closes on 30 June. The Google Form visible without JavaScript is titled "Kotlin Foundation Ecosystem Grant Proposal 2026" and asks for repository URL, open-source license, active maintenance confirmation, and Code of Conduct status.

Questions can be directed to the Kotlinlang Slack `#kotlin-foundation` channel or `grants@kotlinfoundation.org`.

## Terms And Expectations

The grant agreement reference expects recipients to follow applicable Kotlin ecosystem guidelines, including:

- Kotlin brand usage guidelines.
- Code quality and documentation standards.
- Kotlin coding conventions.
- Maintenance commitments.
- Applicable codes of conduct.

The 2025 announcement also highlighted permissive open-source licensing and active maintenance as eligibility factors. AdaptiveKt currently uses Apache License 2.0, which is a permissive license.

## Library Best Practices Checklist

The Kotlin library authors guide emphasizes:

- A clearly defined problem domain.
- Appropriate non-functional quality criteria.
- Minimal mental complexity.
- Backward compatibility and clear migration communication.
- Informative documentation with examples.

Kotlin coding conventions additionally recommend library authors explicitly specify public member visibility and return types, and document public members with KDoc where appropriate.

## AdaptiveKt Readiness Checklist

| Requirement / Expectation | Status | Notes |
| --- | --- | --- |
| Open-source repository | Fulfilled | GitHub repo is public. |
| Permissive license | Fulfilled | Apache License 2.0 exists. |
| Published artifact | Fulfilled | `0.1.0-alpha01` is published to Maven Central. |
| Multiplatform relevance | Fulfilled | Library targets Desktop/JVM, Android, Web/Wasm, and iOS artifacts. |
| Common ecosystem problem | Fulfilled | Responsive/productivity/admin UI patterns remain costly in Compose Multiplatform. |
| Documentation site | Fulfilled | GitHub Pages docs-site exists. |
| Demo | Fulfilled | Admin demo exists for Desktop and Web/Wasm. |
| Code of Conduct | Added in this PR | `CODE_OF_CONDUCT.md` created. |
| Contributing guide | Added in this PR | `CONTRIBUTING.md` created. |
| Security reporting | Added in this PR | `SECURITY.md` created. |
| Support expectations | Added in this PR | `SUPPORT.md` created. |
| Roadmap | Added in this PR | `ROADMAP.md` created. |
| Issue and PR templates | Added in this PR | GitHub templates created. |
| Active maintenance evidence | Partial | Recent commits, CI, Maven publishing workflow, docs and demos exist; community adoption metrics should not be invented. |
| KDoc coverage | Partial | Public API has some KDoc; broader coverage remains roadmap work. |
| Accessibility review | Partial | Some focus/pointer/theming work exists, but a formal accessibility pass remains planned. |
| iOS validation | Partial | iOS targets exist; complete validation needs macOS runner/user verification. |

## Actions Taken In This PR

- Rewrote README for clearer positioning, installation, platform support, alpha status, roadmap, and sustainability.
- Corrected stale README text that said Maven Central was not available.
- Added community documents:
  - `CODE_OF_CONDUCT.md`
  - `CONTRIBUTING.md`
  - `SECURITY.md`
  - `SUPPORT.md`
  - `ROADMAP.md`
  - `GOVERNANCE.md`
- Added grant proposal draft.
- Added GitHub issue templates and pull request template.
- Updated repository metadata with a clearer description, homepage, and topics.

## Pending Before Application Submission

- Review the proposal draft with the maintainer's preferred budget, schedule, and milestone dates.
- Confirm whether GitHub Discussions are enabled before referencing them as active support infrastructure.
- Add final links to Maven Central artifact pages if desired.
- Ensure security contact email is acceptable for public documentation.
- Run a final link check after GitHub Pages refreshes.
- Consider adding a short "Grant milestones" issue or project board if requested by the application.

## Notes On Claims

This audit intentionally avoids unsupported claims about:

- Production stability.
- Enterprise adoption.
- Download counts.
- Star counts.
- Company users.
- Guaranteed compatibility before stable releases.

AdaptiveKt should be presented as an alpha but real project: published, documented, multiplatform-oriented, actively maintained, and ready for focused investment.
