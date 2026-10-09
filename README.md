# AtelierTrack — offline repair-ticket tracker for Algerian repair shops

Offline-first Android app (Kotlin, Jetpack Compose, Material 3, Room) for phone and
appliance repair shops in Algeria. Strictly repair-ticket workflow — not a generic CRM.

## Features
- Repair tickets: client (name, phone), device (brand, model, type), fault, status
  (`recu` → `en_cours` → `pret` → `livre`), spare-parts notes, price/deposit in DZD
- Ticket search (client, phone, device, fault) + status filter chips
- PDF receipt generation and share (`PdfDocument`, no dependency, offline)
- French default + Arabic (RTL) via in-app language setting (DataStore)
- 100% offline: Room is the single source of truth, no account, no network permission

## Stack
- Kotlin 1.9.24, AGP 8.5.2, Gradle 8.7, JDK 17
- Compose BOM 2024.06.00, Material 3, Navigation Compose 2.7.7
- Room 2.6.1 (KSP), DataStore Preferences 1.1.1
- minSdk 26, targetSdk/compileSdk 34

## Build
CI (`.github/workflows/android.yml`) runs on every push to `main`:
`testDebugUnitTest` → `lintDebug` → `assembleDebug` → uploads `app-debug.apk`.

Local build needs Android SDK + JDK 17:
```
gradle :app:assembleDebug
```

## Project layout
```
app/src/main/java/dz/ateliertrack/
  MainActivity.kt  AtelierTrackApp.kt
  data/  Ticket.kt  TicketDao.kt  AppDatabase.kt  TicketRepository.kt
  ui/  AtelierNav.kt  TicketViewModel.kt  screens/  theme/
  util/  TicketValidator.kt  PriceFormatter.kt  PdfReceipt.kt  LocaleHelper.kt
```

## Limitations
- Single device only (no sync); uninstalling wipes data (no backup integration yet)
- Receipt PDF is text-only (no logo/photos); Arabic PDF uses system fonts
- Phone field is free text (no DZ number validation); no SMS notifications (offline)

## Costs
- Build + hosting: $0 (GitHub Actions free tier, no backend, no services).

## First 10 customers plan
1. Two phone-repair counters in Algiers (Bab Ezzouar / Belcourt) — free pilot, intake 20 tickets each.
2. Collect feedback on receipt wording (FR/AR) and status names.
3. Appliance shops (TV/electromenager) in Oran + Constantine via repair Facebook groups.
4. Offer printed QR (Play link / APK) for the counter; onboard in 15 min, no account needed.
5. Convert pilots to paid setup/support (e.g. 5,000–10,000 DZD one-time) after 30 days of use.

## License
MIT — see `LICENSE`. Clean-room code, no copied sources.
