# Phase 4.4 — Column Sorting & Configuration Plan

> Estado: **plan / diseño únicamente.** No implementa código productivo. No implementa UI, snapshots remotos ni paginación.

- **Rama:** `feat/data-column-state-foundation`
- **Base:** `origin/main` (post PR #17 + PR #20)
- **Antecedente:** spike previo `spike/data-column-sorting-config-opencode` — auditado y descartado.

## 0. Lecciones del spike fallido (no repetir)

1. **No redeclarar `AdaptiveSortDirection`.** Ya existe en `AdaptiveDataTypes.kt:53` y se reutiliza en `AdaptiveQueryState.sortDirection`, `AdaptiveQueryState.withSort(...)` y `AdaptiveSortOption`. Redeclararlo en el mismo paquete provoca `Redeclaration` (compilación rota).
2. **No partir de la premisa de "tipos faltantes".** El inventario vivo (sección 1) es la fuente de verdad.
3. **No colapsar tipos + resolución en un solo archivo.** Mantener responsabilidades separadas para favorecer testeabilidad y revisiones focales.
4. **No mezclar "sorting" con "configuración de columnas".** Son dos concerns distintos con invariantes distintos.
5. **Helpers primero, tests después, UI al final.** El primer entregable compilable debe ser puro Kotlin multiplatform, sin dependencias de Compose.
6. **Cuidado con la precedencia de `to` infijo vs `?:`.** `a to b ?: c` se parsea como `(a to b) ?: c`. En resolución, envolver siempre con paréntesis explícitos o usar `Pair(...)` con `?:`.
7. **Invariante "máx 3 niveles de prioridad" debe	imponerse en receptores / builders y en cada función de mutación.** Asignar `Primary` a `index >= 3` rompe la invariante.
8. **Nada de trabajo sin commitear.** Todo entregable de este plan se confirma en rama dedicada; no se aceptan archivos untracked.

## 1. Inventario existente de API (verificado en `e6fa248`)

| Tipo / función | Ubicación | Notas |
|---|---|---|
| `enum class AdaptiveSortDirection { Ascending, Descending }` | `AdaptiveDataTypes.kt:53` | **Reutilizar.** No redefinir. |
| `data class AdaptiveQueryState(search, filters, sortKey: String?, sortDirection: AdaptiveSortDirection, page, pageSize)` | `AdaptiveDataTypes.kt:58` | Ya soporta ordenamiento simple. |
| `fun AdaptiveQueryState.withSort(key: String?, direction: AdaptiveSortDirection = sortDirection): AdaptiveQueryState` | `AdaptiveDataTypes.kt:220` | Resetea `page=1`. |
| `fun AdaptiveQueryState.withSearch(...)` | `AdaptiveDataTypes.kt:207` | |
| `fun AdaptiveQueryState.withFilter(...)` | `AdaptiveDataTypes.kt:211` | |
| `fun AdaptiveQueryState.withPage(...)` / `withPageSize(...)` | `AdaptiveDataTypes.kt:227/231` | |
| `data class AdaptiveSortOption(key: String, label: String)` | `AdaptiveDataTypes.kt:74` | Solo metadatos para menú; no state. |
| `data class AdaptiveDataColumn<T>(id, header, minBreakpoint, weight, mobileRole, mobilePriority, showInMobileCard, cell)` | `AdaptiveDataTypes.kt:127` | **No debe modificarse en Phase 4.4 Step 1** (rompería API pública). |
| `fun <T> visibleColumnsForBreakpoint(columns, breakpoint)` | `AdaptiveDataTypes.kt:236` | Solo filtra por breakpoint; no por config hoisted. |
| `AdaptiveDataView(..., queryState, onQueryChange, sortOptions, ...)` | `AdaptiveDataView.kt:83/128/282/381/588/etc.` | Varargs / overloads existentes. |
| `AdaptiveDataSelectionMode`, `AdaptiveDataSelectionState`, `AdaptiveDataBulkActionBar`, `AdaptiveDataSelectAllCheckbox`, `AdaptiveDataViewCompileTest` | varios en `adaptive-data` | Trabajo previo de selección (no se toca en 4.4). |

**No existen hoy:** `AdaptiveSortPriority`, `AdaptiveColumnSortState`, `AdaptiveDataSortState`, `AdaptiveColumnPin`, `AdaptiveColumnConfig`, `AdaptiveDataColumnConfigState`, ni funciones puras de resolución de ordenamiento multicolumna o de configuración de columnas.

## 2. Tipos aditivos propuestos (Step 1 — solo agregan, no modifican API existente)

Todos en paquete `io.github.adaptivekt.data`, en `adaptive-data/src/commonMain`. **Cero dependencias de Compose.** **Cero tipos redeclarados.**

### 2.1 Sorting — `AdaptiveDataSortTypes.kt`

```kotlin
package io.github.adaptivekt.data

// REUTILIZA AdaptiveSortDirection existente (AdaptiveDataTypes.kt:53). No redefinirlo.

public enum class AdaptiveSortPriority {
    Primary,
    Secondary,
    Tertiary,
    ;
    public companion object {
        public const val MAX_RANK: Int = 3
        public fun at(index: Int): AdaptiveSortPriority? =
            values().getOrNull(index.coerceIn(0, MAX_RANK - 1))
    }
}

public data class AdaptiveColumnSortState(
    val columnId: String,
    val direction: AdaptiveSortDirection = AdaptiveSortDirection.Ascending,
    val priority: AdaptiveSortPriority = AdaptiveSortPriority.Primary,
)

public data class AdaptiveDataSortState(
    val sortedColumns: List<AdaptiveColumnSortState> = emptyList(),
) {
    public val primarySort: AdaptiveColumnSortState? get() =
        sortedColumns.firstOrNull { it.priority == AdaptiveSortPriority.Primary }
    public val secondarySort: AdaptiveColumnSortState? get() =
        sortedColumns.firstOrNull { it.priority == AdaptiveSortPriority.Secondary }
    public val tertiarySort: AdaptiveColumnSortState? get() =
        sortedColumns.firstOrNull { it.priority == AdaptiveSortPriority.Tertiary }
    public val isSorted: Boolean get() = sortedColumns.isNotEmpty()

    init {
        require(sortedColumns.size <= AdaptiveSortPriority.MAX_RANK) {
            "AdaptiveDataSortState supports at most ${AdaptiveSortPriority.MAX_RANK} sort columns, got ${sortedColumns.size}"
        }
    }
}
```

> Nota: el `init { require(...) }` impide invariante por construcción. Delegar a los builders para subdivisiones intermedias (restablecer antes de construir el estado final).

### 2.2 Column config — `AdaptiveDataColumnConfigTypes.kt`

```kotlin
package io.github.adaptivekt.data

public enum class AdaptiveColumnPin {
    None,
    Start,
    End,
}

public data class AdaptiveColumnConfig(
    val columnId: String,
    val visible: Boolean = true,
    val pinned: AdaptiveColumnPin = AdaptiveColumnPin.None,
    val order: Int = Int.MAX_VALUE,
    val sortable: Boolean = true,
    val resizable: Boolean = true,
    val width: Int? = null,
)

public data class AdaptiveDataColumnConfigState(
    val columns: List<AdaptiveColumnConfig> = emptyList(),
) {
    public val visibleColumns: List<AdaptiveColumnConfig>
        get() = columns.filter { it.visible }.sortedBy { it.order }
    public val pinnedStart: List<AdaptiveColumnConfig>
        get() = columns.filter { it.visible && it.pinned == AdaptiveColumnPin.Start }.sortedBy { it.order }
    public val pinnedEnd: List<AdaptiveColumnConfig>
        get() = columns.filter { it.visible && it.pinned == AdaptiveColumnPin.End }.sortedBy { it.order }
    public val unpinned: List<AdaptiveColumnConfig>
        get() = columns.filter { it.visible && it.pinned == AdaptiveColumnPin.None }.sortedBy { it.order }

    public fun getConfig(columnId: String): AdaptiveColumnConfig? =
        columns.firstOrNull { it.columnId == columnId }

    public val allColumnIds: List<String> get() = columns.map { it.columnId }
    public val visibleColumnIds: List<String> get() = visibleColumns.map { it.columnId }
}
```

### 2.3 Factory opcional (sin tocar `AdaptiveDataColumn<T>`)

```kotlin
// AdaptiveDataColumnConfigFactory.kt
public fun AdaptiveDataColumnConfigState(
    columnIds: List<String>,
    sortable: (String) -> Boolean = { true },
): AdaptiveDataColumnConfigState = AdaptiveDataColumnConfigState(
    columns = columnIds.mapIndexed { index, id ->
        AdaptiveColumnConfig(columnId = id, order = index, sortable = sortable(id))
    }
)
```

> Esto evita el bug del spike anterior, donde `createColumnConfigState` forzaba `sortable=true` para todas las columnas. Aquí el caller provee la matriz de capabilities por id sin tocar `AdaptiveDataColumn<T>`.

## 3. Reuso explícito de `AdaptiveSortDirection`

- **No** se declara un nuevo enum de dirección. Toda firma (resolución, toggle, factories) usa `AdaptiveSortDirection` existente.
- `AdaptiveColumnSortState.direction: AdaptiveSortDirection` hace referencia al tipo ya vivo en `AdaptiveDataTypes.kt:53`.
- Los helpers que devuelven `Pair<String?, AdaptiveSortDirection>` (p.ej. para alimentar `AdaptiveQueryState.withSort`) devuelven un `Pair` donde el segundo componente es el tipo accessor existente.

## 4. Comportamiento de ordenamiento (helpers puros — `AdaptiveDataSortResolution.kt`)

```kotlin
public fun resolveEffectiveSortState(
    sortState: AdaptiveDataSortState,
    columnConfigs: AdaptiveDataColumnConfigState,
): List<AdaptiveColumnSortState> =
    sortState.sortedColumns
        .filter { columnConfigs.getConfig(it.columnId)?.sortable == true }
        .sortedBy { it.priority.ordinal }
        .distinctBy { it.columnId }
        .take(AdaptiveSortPriority.MAX_RANK)

public fun resolveQuerySortFromState(
    sortState: AdaptiveDataSortState,
    columnConfigs: AdaptiveDataColumnConfigState,
): Pair<String?, AdaptiveSortDirection> {
    val effective = resolveEffectiveSortState(sortState, columnConfigs)
    val primary = effective.firstOrNull()
        ?: return Pair(null, AdaptiveSortDirection.Ascending)
    return Pair(primary.columnId, primary.direction)
}

public fun resolveSortStateFromQuery(
    sortKey: String?,
    sortDirection: AdaptiveSortDirection,
    columnConfigs: AdaptiveDataColumnConfigState,
): AdaptiveDataSortState {
    if (sortKey == null) return AdaptiveDataSortState()
    if (columnConfigs.getConfig(sortKey)?.sortable != true) return AdaptiveDataSortState()
    return AdaptiveDataSortState(
        listOf(AdaptiveColumnSortState(sortKey, sortDirection, AdaptiveSortPriority.Primary))
    )
}

public fun AdaptiveQueryState.toSortState(
    columnConfigs: AdaptiveDataColumnConfigState,
): AdaptiveDataSortState =
    resolveSortStateFromQuery(sortKey, sortDirection, columnConfigs)

public fun AdaptiveQueryState.withSortState(
    sortState: AdaptiveDataSortState,
    columnConfigs: AdaptiveDataColumnConfigState,
): AdaptiveQueryState {
    val (key, direction) = resolveQuerySortFromState(sortState, columnConfigs)
    return withSort(key, direction)
}
```

> `resolveQuerySortFromState` usa `Pair(null, AdaptiveSortDirection.Ascending)` explícito (no `to ... ?: ...`) — corrige el bug de precedencia del spike previo.

### Toggle (semántica bien definida)

`AdaptiveDataSortState.toggleColumnSort(columnId, configs, defaultDirection)`:

- Si la columna no es sortable o no existe → retorna `this`.
- Si la columna ya está en el estado: invierte `Ascending ↔ Descending`.
  - Mantiene su posición en `sortedColumns` (no la sube a posición 0 por la rotura L1 del spike previo).
  - Si era `Secondary` o `Tertiary`, subirla a `Primary` se hace **vía función separada** `promoteToPrimary(columnId)`, no como side-effect del toggle.
- Si la columna no está: la agrega al frente como `Primary` y degrada al `Primary` anterior a `Secondary`, `Secondary` a `Tertiary`, descartando el `Tertiary` anterior.
- Resultado siempre `take(MAX_RANK)` y repriorizado: `[Primary, Secondary, Tertiary]`.

### Cycle policy (declarada en KDoc)

Sin flag externo: cycle = `Asc → Desc → remove`. Si más adelante se requiere `Asc ↔ Desc` sin remoción, se introduce `AdaptiveSortCycle` enum en Step 2; **no** se cambia la firma actual.

### Helpers derivados

```kotlin
public fun AdaptiveDataSortState.removeColumnSort(columnId: String): AdaptiveDataSortState
public fun AdaptiveDataSortState.promoteToPrimary(columnId: String): AdaptiveDataSortState
public fun AdaptiveDataSortState.reorderSortPriority(fromIndex: Int, toIndex: Int): AdaptiveDataSortState
public fun AdaptiveDataSortState.setDirection(columnId: String, direction: AdaptiveSortDirection): AdaptiveDataSortState
```

`reorderSortPriority` valida `fromIndex`/`toIndex` en rango y **no** emite `Primary` para `index >= MAX_RANK`: en su lugar `take(MAX_RANK)` y `require(...)` en el constructor.

## 5. Comportamiento de visibilidad / orden de columnas (`AdaptiveDataColumnConfigResolution.kt`)

```kotlin
public fun AdaptiveDataColumnConfigState.toggleVisibility(columnId: String): AdaptiveDataColumnConfigState
public fun AdaptiveDataColumnConfigState.setVisible(columnId: String, visible: Boolean): AdaptiveDataColumnConfigState
public fun AdaptiveDataColumnConfigState.setPin(columnId: String, pin: AdaptiveColumnPin): AdaptiveDataColumnConfigState
public fun AdaptiveDataColumnConfigState.moveColumn(columnId: String, newIndex: Int): AdaptiveDataColumnConfigState
public fun AdaptiveDataColumnConfigState.setWidth(columnId: String, width: Int?): AdaptiveDataColumnConfigState
public fun AdaptiveDataColumnConfigState.resetToDefaults(defaults: List<AdaptiveColumnConfig>): AdaptiveDataColumnConfigState
```

Comportamiento clave:

- `setPin` solo afecta columnas visibles; cola `End` y pila `Start` son mutuamente excluyentes por lado: al fijar `pin = Start` de `columnId`, las demás visibles con `pinned == Start` se cambian a `None`. Análogo para `End`. `pin == None` no recorre otras columnas (fix L4 del spike previo).
- `moveColumn` opera solo sobre `visibleColumns`, normaliza `order` a índices enteros contiguos `0..visibleColumns.size-1`, y deja hidden columns con `order = Int.MAX_VALUE` al final de la lista.
- `resetToDefaults` mergea por `columnId` (preserva columnas custom no presentes en defaults) y resetea `visible`, `pinned`, `order`, `sortable`, `resizable`, `width`.

## 6. Test plan (`commonTest`, sin Compose)

> **Sin tests en commonTest → Phase 4.4 no se considera done.** Lección D3 del spike previo.

Archivos:
- `AdaptiveDataSortTypesTest.kt`
- `AdaptiveDataSortResolutionTest.kt`
- `AdaptiveDataColumnConfigTest.kt`
- `AdaptiveDataColumnConfigResolutionTest.kt`
- `AdaptiveDataSortRoundTripTest.kt` (integración vs `AdaptiveQueryState.withSort`)

### Casos mínimos de sort

1. `resolveEffectiveSortState` filtra columnas no-sortables.
2. `resolveEffectiveSortState` ordena por `priority.ordinal` y `distinctBy(columnId)`.
3. `resolveQuerySortFromState` devuelve `Pair(null, Ascending)` cuando no hay primary.
4. `resolveQuerySortFromState` devuelve `Pair(columnId, direction)` cuando hay primary.
5. `resolveQuerySortFromState` no emite `Pair<String?, AdaptiveSortDirection?>` (regresión bug E2/E3 del spike previo — assertion explícita con `assertSame` de tipo).
6. `resolveSortStateFromQuery(null, _, _)` ⇢ estado vacío.
7. `resolveSortStateFromQuery` con columna no-sortable ⇠ estado vacío.
8. `resolveSortStateFromQuery` round-tripa con `withSortState`.
9. `toggleColumnSort` primera vez ⇠ `[Primary]`.
10. `toggleColumnSort` segunda vez invierte dirección sin cambiar posición.
11. `toggleColumnSort` agrega nueva columna como `Primary` y degrada las anteriores.
12. `toggleColumnSort` respeta `MAX_RANK = 3` (la 4ª columna desplaza a `Tertiary`).
13. `toggleColumnSort` ignora columna no-sortable.
14. `removeColumnSort` reprioriza (0→Primary, 1→Secondary, 2→Tertiary).
15. `promoteToPrimary` sube y reordena sin perder columnas.
16. `reorderSortPriority` valida out-of-range sin cambios.
17. `reorderSortPriority` con `take(3)` no introduce `Primary` fantasma en `index≥3`.
18. `AdaptiveDataSortState` con 4 columnas lanzó `IllegalArgumentException` (regresión L3 spike previo).

### Casos mínimos de column config

1. `toggleVisibility` solo cambia la columna indicada.
2. `setPin(Start)` mueve otras `Start` visibles a `None`; respeta `End`.
3. `setPin(End)` análogo.
4. `setPin(None)` no recorre otras columnas (regresión L4 spike previo).
5. `moveColumn` normaliza `order` contiguo.
6. `moveColumn` con `newIndex` fuera de rango hace clamp.
7. `setWidth(null)` limpia.
8. `resetToDefaults` preserva columnas custom.
9. `AdaptiveDataColumnConfigState(columnIds, sortable)` respeta la matriz de capabilities (regresión `createColumnConfigState` spike previo que forzaba `sortable=true`).

### Casos de integración

1. `AdaptiveQueryState().withSort("x", Descending).toSortState(cfg).withSortState(cfg)` ⇠ estado equivalente en `sortKey`/`sortDirection`.
2. Estado multicolumna `withSortState` solo proyecta `primary` en el `AdaptiveQueryState`; la multicolumna vive en hoisted state.

## 7. Migration path

- **Step 1 (este plan):** Solo `commonMain` types puros + helpers + `commonTest`. Cero cambios en `AdaptiveDataTypes.kt`, `AdaptiveDataView.kt`, ni `AdaptiveDataColumn<T>`. Compila con `gradlew :adaptive-data:compileKotlinJvm` / `compileKotlinWasmJs`.
- **Step 2 (futura rama `feat/...`):** Integrar overloads *aditivos* en `AdaptiveDataView`:
  `sortState: AdaptiveDataSortState? = null`, `onSortStateChange: ((AdaptiveDataSortState) -> Unit)? = null`.
  Cuando ambos son `null` → comportamiento actual (sin breaks).
- **Step 3:** Introducir `columnConfigState` / `onColumnConfigStateChange` en `AdaptiveDataView`; render header menu con pin/visibility/order usando `AdaptiveAnchoredMenuBox` ya existente en `adaptive-components`.
- **Step 4:** Persistencia opcional (persister caller-owned, no en `adaptive-data`).
- **Step 5:** Remote snapshots, paginación — fuera de alcance aquí (ver non-goals).

No hay Step 2-5 en esta rama; esta rama solo entrega Step 1 con el plan.

## 8. Riesgos

| Riesgo | Mitigación |
|---|---|
| Redeclaración accidental de `AdaptiveSortDirection` | Test de regression resolveQuerySortFromState tipa `Pair<String?, AdaptiveSortDirection>` (sin `?`). |
| Invariante `MAX_RANK = 3` rota por builder intermedio | `init { require(...) }` en `AdaptiveDataSortState`; helpers siempre `take(MAX_RANK)`. |
| `AdaptiveQueryState.withSort` muta `page=1` y rompe integración multicolumna | Documentar: el query sólo refresca page al cambiar primary, no al toggle de secundaria (vive en UI state). |
| `AdaptiveDataColumn<T>` carece de metadata `sortable` | No tocar en Step 1; capability se provee vía `AdaptiveColumnConfig.sortable` desde caller. |
| `AdaptiveDataView` crece en overloads | Step 2 introducirá el overload minimal con null/optional para preservar bincompat. |
| Estado 2-source (sortState vs queryState.sortKey) | Documentar ownership: `sortState` es hoisted source-of-truth; `queryState.sortKey` es derivado solo para backends legacy. Tests round-trip cubren. |
| Conflictos con branch vivo de selección (`feat/production-data-workflows`) | Base rebase-safe: solo se agregan archivos en `adaptive-data/src/commonMain` y `commonTest`. Sin toques a `AdaptiveDataView.kt`. |
| Trabajo sin commitear (spike previo) | Política: nada untracked al finalizar fase; `git status --short` debe estar limpio. |

## 9. Non-goals (fase 4.4 Step 1)

- **No** implementar UI de headers, menús de columna, ni Composables nuevos.
- **No** modificar `AdaptiveDataColumn<T>` (mantiene API binaria).
- **No** modificar `AdaptiveQueryState` fields existentes; solo agregar extensiones.
- **No** modificar `AdaptiveDataView.kt` (sección crítica de PR #17 / production-data-workflows).
- **No** implementar remote snapshots.
- **No** implementar paginación.
- **No** implementar persistencia de config (aún).
- **No** introducir cycle policy configurable (DefaultValue `Asc → Desc → remove`).
- **No** exponer tipos nuevos fuera de `io.github.adaptivekt.data`.
- **No** tocar `adaptive-components` (riesgo de CI WebGL de PR #17).

---

## Implementation spike status

- Initial implementation added pure sort and column config state with helpers and tests; compiled and passed `:adaptive-data:jvmTest`, `:adaptive-data:wasmJsTest` and `:adaptive-data:build`.
- Audit found hardening items `I1`–`I6` plus minor `M3` (compile fixture not executed).
- Hardening pass addresses:
  - **sort state invariants** — `AdaptiveDataSortState` now enforces max `MAX_RANK` columns, unique column ids and unique priorities via `init { require(...) }`; `AdaptiveSortPriority` exposes `MAX_RANK` and `fromIndex(index)` with no `Primary` fallback for invalid indices;
  - **Secondary/Tertiary toggle behavior** — `toggleColumnSort` now preserves the column's relative position and priority when flipping `Ascending` to `Descending`; promotion to `Primary` is delegated to the separate `promoteColumnSort` helper, matching the plan;
  - **hidden pin normalization** — `normalizeAdaptiveDataColumnConfigState` now clears pins from hidden columns and resolves `Start`/`End` only among visible columns, so hidden columns never consume or steal a pin slot;
  - **pin winner by visible order** — when multiple visible columns share a `Start` (or `End`) pin, the one with the lowest `order` wins, regardless of input list order;
  - **query integration helpers** — added `resolveEffectiveSortState`, `resolveQuerySortFromState`, `resolveSortStateFromQuery`, `AdaptiveQueryState.toSortState` and `AdaptiveQueryState.withSortState`, all using explicit `Pair(...)` to avoid the `to`/`?:` precedence bug;
  - **accessors** — `AdaptiveDataSortState` exposes `primarySort`, `secondarySort`, `tertiarySort`, `isSorted`; `AdaptiveDataColumnConfigState` exposes `visibleColumns`, `hiddenColumns`, `visibleColumnIds`, `hiddenColumnIds` and `getConfig(columnId)`;
  - **compile fixture execution** — `compileColumnStateTypes` is now invoked from `passesCompilation()` and exercises the new helpers (`setColumnSortDirection`, `reorderSortPriority`, `resolveQuerySortFromState`, `toSortState`, `withSortState`, `setColumnWidth`, `resetAdaptiveDataColumnConfigState`, etc.).
- Additional helpers added: `setColumnSortDirection`, `reorderSortPriority`, `setColumnWidth`, `resetAdaptiveDataColumnConfigState`, plus the `width: Int?` property on `AdaptiveColumnConfig` (additive, last position).
- UI remains a **non-goal** for this step.
- PR #17 (`feat/production-data-workflows`) remains untouched; no merge, rebase or force push was performed.

---

## Cierre

Entregable de esta rama: este documento. Implementación y tests viven en ramas futuras derivadas de este plan.

Restricciones respetadas: no main, no PR #17, no merge, no rebase, no force push, no `git add .`, no `git add -A`.