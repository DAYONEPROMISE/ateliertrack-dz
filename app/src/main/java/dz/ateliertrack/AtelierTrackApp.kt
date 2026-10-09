package dz.ateliertrack

import android.app.Application
import dz.ateliertrack.data.AppDatabase
import dz.ateliertrack.data.TicketRepository

/** Application entry point. Holds the Room database and repository (100% offline, no account). */
class AtelierTrackApp : Application() {
    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { TicketRepository(database.ticketDao()) }
}
