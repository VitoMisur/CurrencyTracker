package com.vito.misur.currencytracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vito.misur.currencytracker.network.data.Currency

const val DATABASE_NAME = "currency-tracker-db"

/**
 * Application database
 * with 2 tables containing Currency and FavoriteCurrency data
 */
@Database(entities = [Currency::class, FavoriteCurrency::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun supportedCurrenciesDao(): SupportedCurrenciesDao
    abstract fun favoriteCurrenciesDao(): FavoriteCurrenciesDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create the database.
        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                })
                .build()
        }
    }
}