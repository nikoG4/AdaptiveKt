import re
import sys
import os
from pathlib import Path

def check_registry():
    workspace_root = Path(os.path.dirname(os.path.realpath(__file__))).parent
    registry_path = workspace_root / "docs-site" / "src" / "commonMain" / "kotlin" / "io" / "github" / "adaptivekt" / "site" / "DocsRegistry.kt"
    components_page_path = workspace_root / "docs-site" / "src" / "commonMain" / "kotlin" / "io" / "github" / "adaptivekt" / "site" / "SiteComponentsPage.kt"

    failed = False

    if not registry_path.exists():
        print(f"ERROR: {registry_path} does not exist.")
        failed = True
    if not components_page_path.exists():
        print(f"ERROR: {components_page_path} does not exist.")
        failed = True

    if failed:
        sys.exit(1)

    registry_content = registry_path.read_text(encoding='utf-8')
    components_content = components_page_path.read_text(encoding='utf-8')

    # Extract ID constants: const val ID_BUTTON = "adaptive-button"
    id_pattern = re.compile(r'const\s+val\s+(ID_[A-Z0-9_]+)\s*=\s*"([^"]+)"')
    declared_ids = {}
    for match in id_pattern.finditer(registry_content):
        name = match.group(1)
        val = match.group(2)
        declared_ids[name] = val

    if len(declared_ids) == 0:
        print("ERROR: No ID_* constants found in DocsRegistry.kt.")
        failed = True

    kebab_pattern = re.compile(r'^[a-z0-9]+(?:-[a-z0-9]+)*$')
    val_to_name = {}
    for name, val in declared_ids.items():
        if not val.strip():
            print(f"ERROR: Empty literal value for {name}.")
            failed = True
        elif not kebab_pattern.match(val):
            print(f"ERROR: Value for {name} ('{val}') is not kebab-case.")
            failed = True
        
        if val in val_to_name:
            print(f"ERROR: Duplicate literal value '{val}' for {name} and {val_to_name[val]}.")
            failed = True
        else:
            val_to_name[val] = name

    # Extract references: id = DocsRegistry.ID_BUTTON or DocsRegistry.ID_BUTTON
    ref_pattern = re.compile(r'DocsRegistry\.(ID_[A-Z0-9_]+)')
    referenced_ids = set(ref_pattern.findall(components_content))

    if len(referenced_ids) == 0:
        print("ERROR: No references to DocsRegistry.ID_* found in SiteComponentsPage.kt.")
        failed = True

    for ref in referenced_ids:
        if ref not in declared_ids:
            print(f"ERROR: SiteComponentsPage.kt references undefined constant {ref}.")
            failed = True

    for name in declared_ids.keys():
        if name not in referenced_ids:
            print(f"ERROR: Constant {name} is defined in DocsRegistry.kt but never used in SiteComponentsPage.kt.")
            failed = True

    if failed:
        print("Docs site registry guards failed.")
        sys.exit(1)
    else:
        print("Docs site registry guards passed.")
        sys.exit(0)

if __name__ == '__main__':
    check_registry()
