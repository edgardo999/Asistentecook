package com.example.asistentedecocina.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.asistentedecocina.presentation.screens.main.MainScreen
import com.example.asistentedecocina.presentation.screens.recipes.RecipesScreen
import com.example.asistentedecocina.presentation.screens.recipes.cooking.CookingModeScreen
import com.example.asistentedecocina.presentation.screens.recipes.detail.RecipeDetailScreen
import com.example.asistentedecocina.presentation.viewmodel.RecipesViewModel

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Recipes : Screen("recipes")
    object RecipeDetail : Screen("recipe/{recipeId}") {
        fun createRoute(recipeId: Long) = "recipe/$recipeId"
    }
    object CookingMode : Screen("cooking/{recipeId}") {
        fun createRoute(recipeId: Long) = "cooking/$recipeId"
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    recipesViewModel: RecipesViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                onNavigateToRecipes = {
                    navController.navigate(Screen.Recipes.route)
                }
            )
        }

        composable(Screen.Recipes.route) {
            RecipesScreen(
                viewModel = recipesViewModel,
                onRecipeClick = { recipeId ->
                    navController.navigate(Screen.RecipeDetail.createRoute(recipeId))
                }
            )
        }

        composable(Screen.RecipeDetail.route) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toLongOrNull() ?: return@composable
            val recipe = recipesViewModel.getRecipe(recipeId)

            RecipeDetailScreen(
                recipe = recipe,
                onBackClick = { navController.popBackStack() },
                onEditClick = { /* TODO: Implementar edición */ },
                onFavoriteClick = { recipesViewModel.toggleFavorite(recipeId) },
                onStartCooking = {
                    navController.navigate(Screen.CookingMode.createRoute(recipeId))
                }
            )
        }

        composable(Screen.CookingMode.route) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toLongOrNull() ?: return@composable
            val recipe = recipesViewModel.getRecipe(recipeId)

            CookingModeScreen(
                recipe = recipe,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
} 