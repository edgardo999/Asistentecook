package com.example.asistentedecocina.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.asistentedecocina.data.local.dao.RecipeDao
import com.example.asistentedecocina.data.local.entity.*
import com.example.asistentedecocina.data.model.RecipeDifficulty

@Database(
    entities = [
        RecipeEntity::class,
        RecipeCategoryEntity::class,
        RecipeIngredientEntity::class,
        RecipeCategoryRelation::class,
        FavoriteRecipeEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    @androidx.room.TypeConverter
    fun fromRecipeDifficulty(value: RecipeDifficulty): String {
        return value.name
    }

    @androidx.room.TypeConverter
    fun toRecipeDifficulty(value: String): RecipeDifficulty {
        return RecipeDifficulty.valueOf(value)
    }
} 