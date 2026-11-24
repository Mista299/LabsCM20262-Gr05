package co.edu.udea.compumovil.gr05_20262.lab2.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.edu.udea.compumovil.gr05_20262.lab2.R

/**
 * Raiz de la navegacion de la aplicacion (Navigation de Compose).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.app_title)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.LIST,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.LIST) {
                PostListScreen(
                    onPostClick = { postId ->
                        navController.navigate(Routes.detail(postId))
                    }
                )
            }
            composable(
                route = Routes.DETAIL,
                arguments = listOf(
                    navArgument(Routes.ARG_POST_ID) { type = NavType.IntType }
                )
            ) { entry ->
                val postId = entry.arguments?.getInt(Routes.ARG_POST_ID) ?: 0
                PostDetailScreen(
                    postId = postId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
