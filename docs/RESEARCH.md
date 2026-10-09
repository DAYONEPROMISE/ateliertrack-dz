# RESEARCH — AtelierTrack

Date: 2026-10-09. Sources verified by live web search; no fabricated claims.

## 1. Shop workflow (competitor scan)
- RepairTicket, RepairPOS, Workflow Repair, Horus Repair, Sla7li, SpudgerHQ, PhoneRepairPOS:
  all converge on intake (customer + device + fault) → status pipeline → quote/deposit →
  parts tracking → ready notification → handover + receipt.
- Differentiators we deliberately omit (cloud/SaaS scope): IMEI verification services,
  POS/registers, SMS/email automation, multi-branch, inventory counts. AtelierTrack is
  strictly the repair-ticket workflow, offline, single device.
- Paper-ticket pain confirmed: shops lose track of "who took this phone" and damage
  disputes; our ticket + status + PDF receipt covers the core.

## 2. Algeria specifics
- Counter language is French with Arabic-speaking customers; receipts must read in both.
  => FR default, AR RTL toggle, receipt generated in the active language.
- Currency DZD, cash + deposit ("acompte/orboun") practice => price/deposit/balance fields.
- Connectivity at small counters is unreliable => 100% offline requirement, no account,
  no INTERNET permission at all.

## 3. Tech validation (developer.android.com, Room 2.8.5 release notes seen 2026-09-09)
- Material 3 is the Compose-first design system (M3 Expressive stable 2025–2026).
- Room 2.6.1+ is the stable offline persistence layer; DataStore Preferences for settings.
- Offline-first pattern: UI observes Room Flows; all writes go to Room first.
- PdfDocument (framework) generates receipts with zero dependencies — best for offline APK size.
- Versions pinned after checking stability: AGP 8.5.2 + Kotlin 1.9.24 (Kotlin 2.x + KSP/Room
  friction avoided), Compose BOM 2024.06.00, Room 2.6.1, Gradle 8.7, JDK 17, SDK 34.

## 4. Selected scope (VALIDATE)
Must: tickets CRUD, 4 statuses, parts notes, search, PDF share, FR/AR RTL, offline, no account.
Non-goals v1: sync, photos, SMS, inventory, POS, accounts.
