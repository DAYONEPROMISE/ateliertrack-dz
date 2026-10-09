package dz.ateliertrack.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {
    @Query("SELECT * FROM tickets ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<Ticket>>

    @Query("SELECT * FROM tickets WHERE id = :id")
    fun observeById(id: Long): Flow<Ticket?>

    @Query(
        "SELECT * FROM tickets WHERE " +
            "clientName LIKE '%' || :query || '%' OR " +
            "clientPhone LIKE '%' || :query || '%' OR " +
            "deviceBrand LIKE '%' || :query || '%' OR " +
            "deviceModel LIKE '%' || :query || '%' OR " +
            "faultDescription LIKE '%' || :query || '%' " +
            "ORDER BY updatedAt DESC"
    )
    fun search(query: String): Flow<List<Ticket>>

    @Query("SELECT * FROM tickets WHERE status = :status ORDER BY updatedAt DESC")
    fun observeByStatus(status: String): Flow<List<Ticket>>

    @Query("SELECT COUNT(*) FROM tickets WHERE status = :status")
    suspend fun countByStatus(status: String): Int

    @Insert
    suspend fun insert(ticket: Ticket): Long

    @Update
    suspend fun update(ticket: Ticket)

    @Delete
    suspend fun delete(ticket: Ticket)

    @Query("SELECT * FROM tickets WHERE id = :id")
    suspend fun getById(id: Long): Ticket?
}
