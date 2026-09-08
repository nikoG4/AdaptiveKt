# Security Policy

AdaptiveKt is a UI library, so most issues are not traditional server-side vulnerabilities. Security-relevant reports may still include problems such as unsafe generated markup, dependency or publishing-chain concerns, data exposure in examples, or behavior that could mislead users into unsafe patterns.

## Supported Versions

| Version | Supported |
| --- | --- |
| `0.1.x-alpha` | Security reports accepted; fixes are best-effort while APIs are alpha. |

## Reporting A Vulnerability

Please do not open a public issue for vulnerabilities or sensitive supply-chain concerns.

Use GitHub's private vulnerability reporting for this repository (`Security` → `Report a vulnerability`) when available. If private reporting is temporarily unavailable, do not publish sensitive details; open a minimal public issue asking the maintainer to enable or provide a private reporting channel.

Please include:

- Affected module and version.
- Platform, if relevant.
- Reproduction steps or proof of concept.
- Impact and suggested mitigation, if known.

The maintainer will acknowledge valid private reports when possible and coordinate a fix or disclosure plan appropriate to the severity.

## Scope

In scope:

- AdaptiveKt library modules.
- Published artifacts and metadata.
- Docs or examples if they expose unsafe guidance.

Out of scope:

- Vulnerabilities in unrelated applications built with AdaptiveKt.
- Publicly known issues in third-party tools unless AdaptiveKt can reasonably mitigate them.
- Denial-of-service claims that only affect local demo tooling without project impact.
