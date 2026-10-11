# OpenPencil pass — ateliertrack-dz

## Design (Shared/OpenPencil/ateliertrack-dz/)
- `design.html` (ticket list + filter chips + empty state, FR/AR, DZ tokens) → `design.fig`
- `lint --preset accessibility`: 0 errors, 0 warnings

## Code changes
- 4 screens: all 9 `Icon(..., null)` now carry FR/AR `contentDescription`
  (back, share, edit, delete, settings, add, search). Locale-aware via existing `fr` flag.
- Theme + nav + FR/AR locale helper untouched (already conformant).

## APKs
Replaced the fixed-tag debug-only publish with versioned GitHub Releases
(`v{versionName}-b{run}`): notes + debug + unsigned release APKs.
