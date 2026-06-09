# AI Workspace Layout Guard

**Command:**
```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\check-ai-workspace-layout-guards.ps1
```

**Patterns Checked:**
- `BoxWithConstraints`
- `breakpointForWidth`
- `LocalAdaptiveLayoutInfo.current`
- `AdaptiveBreakpoint.Compact`

**Result:**
`Layout Guard Test PASSED. No manual constraints found.`

**Allowlist:**
None. No manual breakpoint structures are used in the demo.
