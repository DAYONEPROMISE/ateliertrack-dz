package dz.ateliertrack.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dz.ateliertrack.ui.screens.SettingsScreen
import dz.ateliertrack.ui.screens.TicketDetailScreen
import dz.ateliertrack.ui.screens.TicketEditScreen
import dz.ateliertrack.ui.screens.TicketListScreen

object Routes {
    const val LIST = "list"
    const val DETAIL = "detail/{id}"
    const val EDIT = "edit?id={id}"
    const val SETTINGS = "settings"
    fun detail(id: Long) = "detail/$id"
    fun edit(id: Long?) = if (id == null) "edit?id=0" else "edit?id=$id"
}

@Composable
fun AtelierNav(viewModel: TicketViewModel, lang: String) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Routes.LIST) {
        composable(Routes.LIST) {
            TicketListScreen(
                viewModel = viewModel,
                onOpen = { nav.navigate(Routes.detail(it)) },
                onAdd = { nav.navigate(Routes.edit(null)) },
                onSettings = { nav.navigate(Routes.SETTINGS) }
            )
        }
        composable(
            Routes.DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStack ->
            val id = backStack.arguments?.getLong("id") ?: 0L
            val ticket by viewModel.ticketFlow(id).collectAsState(initial = null)
            TicketDetailScreen(
                ticket = ticket,
                onBack = { nav.popBackStack() },
                onEdit = { nav.navigate(Routes.edit(id)) },
                onAdvance = { t -> viewModel.advance(t) },
                onDelete = { t -> viewModel.delete(t); nav.popBackStack() }
            )
        }
        composable(
            Routes.EDIT,
            arguments = listOf(navArgument("id") {
                type = NavType.LongType; defaultValue = 0L
            })
        ) { backStack ->
            val id = backStack.arguments?.getLong("id") ?: 0L
            val ticket by viewModel.ticketFlow(id).collectAsState(initial = null)
            TicketEditScreen(
                existing = if (id == 0L) null else ticket,
                onBack = { nav.popBackStack() },
                onSave = { t -> viewModel.save(t) { nav.popBackStack() } }
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(lang = lang, onBack = { nav.popBackStack() })
        }
    }
}
