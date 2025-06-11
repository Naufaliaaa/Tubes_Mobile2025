package id.ac.unpas.blogging.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import id.ac.unpas.blogging.data.model.Comment
import id.ac.unpas.blogging.data.model.FullPost
import id.ac.unpas.blogging.data.model.Post
import id.ac.unpas.blogging.ui.features.auth.LoginScreen
import id.ac.unpas.blogging.ui.features.auth.RegisterScreen
import id.ac.unpas.blogging.ui.features.create_edit_post.CreateEditPostScreen
import id.ac.unpas.blogging.ui.features.home.HomeScreen
import id.ac.unpas.blogging.ui.features.post_detail.PostDetailScreen
import id.ac.unpas.blogging.ui.theme.BloggingTheme

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN
    ) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(AppRoutes.HOME) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppRoutes.REGISTER)
                }
            )
        }

        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                onRegisterClick = { fullName, email, pass ->
                    println("Register clicked: $fullName, $email, $pass")
                    navController.navigate(AppRoutes.LOGIN) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.HOME) {
            HomeScreen(
                onPostClick = { postId ->
                    navController.navigate(AppRoutes.postDetailRoute(postId))
                },
                onNavigateToCreatePost = {
                    navController.navigate(AppRoutes.CREATE_POST)
                }
            )
        }

        composable(
            route = AppRoutes.POST_DETAIL,
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) {
            PostDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEditPost = { postIdToEdit ->
                    navController.navigate(AppRoutes.editPostRoute(postIdToEdit))
                }
            )
        }

        composable(AppRoutes.CREATE_POST) {
            CreateEditPostScreen(
                onNavigateBack = { navController.popBackStack() },
                onPostSavedOrUpdated = { _ ->
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoutes.EDIT_POST,
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) {
            CreateEditPostScreen(
                onNavigateBack = { navController.popBackStack() },
                onPostSavedOrUpdated = { editedPostIdFromVM ->
                    val originalPostId = it.arguments?.getString("postId")
                    if (originalPostId != null) {
                        navController.popBackStack(route = AppRoutes.postDetailRoute(originalPostId), inclusive = true)
                        navController.navigate(AppRoutes.postDetailRoute(originalPostId))
                    } else {
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppNavHostPreview() {
    val navController = rememberNavController()

    BloggingTheme {
        AppNavHost(navController = navController)
    }
}