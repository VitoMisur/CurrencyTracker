package com.vito.misur.currencytracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteCurrenciesDao {

    @Query("SELECT * FROM favorite_currencies ORDER BY symbol")
    fun getAvailableCurrenciesLiveData(): LiveData<List<FavoriteCurrency>>

    @Query("SELECT * FROM favorite_currencies WHERE base_currency = :baseCurrencySymbol ORDER BY symbol")
    fun getAvailableCurrencies(baseCurrencySymbol: String): List<FavoriteCurrency>

    @Query("SELECT * FROM favorite_currencies WHERE is_favorite = :isFavorite ORDER BY symbol")
    fun getAllFavoritesLiveData(isFavorite: Boolean = true): LiveData<List<FavoriteCurrency>>

    @Query("SELECT * FROM favorite_currencies WHERE is_favorite = :isFavorite AND base_currency = :baseCurrencySymbol ORDER BY symbol")
    fun getAllFavorites(
        baseCurrencySymbol: String,
        isFavorite: Boolean = true
    ): List<FavoriteCurrency>

    @Query("SELECT * FROM favorite_currencies WHERE id = :favoriteCurrencyId")
    fun getAvailableCurrencyLiveData(favoriteCurrencyId: Long): LiveData<FavoriteCurrency>

    @Query("SELECT * FROM favorite_currencies WHERE is_favorite = :isFavorite AND id = :favoriteCurrencyId")
    fun getFavoriteCurrencyLiveData(
        favoriteCurrencyId: Long,
        isFavorite: Boolean = true
    ): LiveData<FavoriteCurrency>

    @Query("SELECT symbol FROM favorite_currencies WHERE is_favorite = :isFavorite")
    fun getAllFavoritesSymbols(isFavorite: Boolean = true): List<String>

    @Query("UPDATE favorite_currencies SET is_favorite = :isFavorite WHERE symbol IN (:symbols)")
    fun restoreFavorites(symbols: List<String>, isFavorite: Boolean = true)

    @Query("UPDATE favorite_currencies SET is_favorite = :isFavorite WHERE id = :currencyId")
    fun setCurrencyAsFavorite(currencyId: Long, isFavorite: Boolean): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAvailableCurrenciesList(availableCurrencies: List<FavoriteCurrency>): List<Long>

    @Delete
    suspend fun deleteFavoriteCurrency(favoriteCurrency: FavoriteCurrency)

    @Delete
    suspend fun deleteMultiple(currencies: List<FavoriteCurrency>)

    @Query("DELETE FROM favorite_currencies")
    fun deleteAll()

    @Transaction
    suspend fun repopulateFavorites(availableCurrencies: List<FavoriteCurrency>) {
        val favorites = getAllFavoritesSymbols()
        deleteAll()
        insertAvailableCurrenciesList(availableCurrencies)
        restoreFavorites(favorites)
    }
}