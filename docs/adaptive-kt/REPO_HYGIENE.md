# Repo Hygiene

PR C6.1 removes generated outputs from the Git index while keeping local files on disk.

## Ignored Outputs

The repository `.gitignore` covers:

```text
.gradle/
build/
**/build/

visual-captures/
visual-captures-components/
**/visual-captures/

*.zip
*.log

*.class
*.jar
```

## Removed From Git Index

The cleanup used `git rm --cached`, so files remain available locally but are no longer tracked by Git.

Removed categories:

- Gradle local cache files under `.gradle/`
- Root build outputs under `build/`
- Module build outputs under `adaptive-*/build/`
- Demo build outputs under `admin-demo/build/`
- Generated visual captures under build folders
- Generated ZIP archives under build folders
- Generated Gradle reports under build folders
- Compiled classes, jars, test reports, and Kotlin compiler caches under build folders

No source files, docs, Gradle wrapper files, scripts, or Markdown files were removed from the index as generated output.

## What Should Be Versioned

Keep these in Git:

- Source code under `src/`
- Gradle build scripts and wrapper files
- `settings.gradle.kts`
- Tools/scripts under `tools/`
- Documentation under `docs/`
- Hand-authored design files or fixtures if intentionally added

## What Should Not Be Versioned

Do not version:

- `.gradle/`
- Any `build/` directory
- Generated screenshots/captures
- Generated ZIP artifacts
- Generated reports under build folders
- Compiled classes, jars, and compiler caches
- Logs

## Regenerating Captures

Main admin-demo captures:

```powershell
.\tools\capture-admin-demo.ps1
```

Components-only captures:

```powershell
.\tools\capture-admin-demo.ps1 -ComponentsOnly -OutputDir build\visual-captures-components -ZipPath build\adaptivekt-components-showcase-captures.zip
```

Generated captures and ZIPs stay local and ignored.
