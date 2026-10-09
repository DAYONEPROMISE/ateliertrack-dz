package dz.ateliertrack.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dz.ateliertrack.data.Ticket
import dz.ateliertrack.data.TicketRepository
import dz.ateliertrack.data.TicketStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TicketViewModel(private val repo: TicketRepository) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _statusFilter = MutableStateFlow<TicketStatus?>(null)
    val statusFilter: StateFlow<TicketStatus?> = _statusFilter

    @OptIn(ExperimentalCoroutinesApi::class)
    val tickets: StateFlow<List<Ticket>> = _query.flatMapLatest { q ->
        if (q.isBlank()) repo.observeAll() else repo.search(q)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setQuery(q: String) { _query.value = q }
    fun setStatusFilter(s: TicketStatus?) { _statusFilter.value = s }

    fun save(ticket: Ticket, onDone: (Long) -> Unit = {}) {
        viewModelScope.launch { onDone(repo.save(ticket)) }
    }

    fun advance(ticket: Ticket) {
        viewModelScope.launch { repo.advanceStatus(ticket) }
    }

    fun delete(ticket: Ticket) {
        viewModelScope.launch { repo.delete(ticket) }
    }

    fun ticketFlow(id: Long) = repo.observeById(id)

    companion object {
        fun factory(repo: TicketRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    TicketViewModel(repo) as T
            }
    }
}
