package com.vito.misur.currencytracker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteCurrenciesDao {

    @Query("SELECT * FROM favorite_currencies ORDER BY symbol")
    fun getAvailableCurrenciesLiveData(): LiveData<List<FavoriteCurrency>>

    /** supported in paid API plan only
    //    @Query("SELECT * FROM favorite_currencies WHERE base_currency = :baseCurrencySymbol ORDER BY symbol")
     **/
    @Query("SELECT * FROM favorite_currencies ORDER BY symbol")
    fun getAvailableCurrencies(
        /** supported in paid API plan only
        baseCurrencySymbol: String,
         **/
    ): List<FavoriteCurrency>

    // TODO: implement currency name (get from Supported Currencies)
    /** supported in paid API plan only
    //    @Query("SELECT * FROM favorite_currencies WHERE base_currency = :baseCurrencySymbol AND symbol LIKE :searchQuery ORDER BY symbol")
     **/
    @Query("SELECT * FROM favorite_currencies WHERE symbol OR name LIKE :searchQuery ORDER BY symbol")
    fun getAvailableCurrenciesFiltered(
        /** supported in paid API plan only
        baseCurrencySymbol: String,
         **/
        searchQuery: String
    ): List<FavoriteCurrency>

    @Query("SELECT * FROM favorite_currencies WHERE is_favorite = :isFavorite ORDER BY symbol")
    fun getAllFavoritesLiveData(isFavorite: Boolean = true): LiveData<List<FavoriteCurrency>>

    @Query("SELECT * FROM favorite_currencies WHERE is_favorite = :isFavorite ORDER BY symbol")
    fun getAllFavorites(
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

    @Query("SELECT id FROM favorite_currencies WHERE is_favorite = :isFavorite")
    fun getAllFavoritesIds(isFavorite: Boolean = true): List<Long>

    @Query("SELECT exchange_rate FROM favorite_currencies WHERE symbol = :mainCurrencySymbol")
    fun getFavoriteExchangeRate(mainCurrencySymbol: String): Double

    @Query("UPDATE favorite_currencies SET is_favorite = :isFavorite WHERE symbol IN (:symbols)")
    fun restoreFavorites(symbols: List<String>, isFavorite: Boolean = true)

    @Query("UPDATE favorite_currencies SET is_favorite = :isFavorite WHERE id = :currencyId")
    fun setCurrencyAsFavorite(currencyId: Long, isFavorite: Boolean): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAvailableCurrenciesList(availableCurrencies: List<FavoriteCurrency>): List<Long>

    @Query("UPDATE favorite_currencies SET base_currency = :baseCurrency WHERE id IN (:ids)")
    fun updateFavoriteCurrenciesById(baseCurrency: String, ids: List<Long>): Int

    @Query("UPDATE favorite_currencies SET calculated_exchange_rate = :calculatedExchangeRate, base_currency = :baseCurrency WHERE id = :id")
    fun updateCalculatedExchangeRate(
        id: Long,
        calculatedExchangeRate: Double,
        baseCurrency: String
    ): Int

    @Transaction
    suspend fun updateFavoriteCurrenciesWithCalculatedValues(mainCurrencySymbol: String): List<FavoriteCurrency> {
        getAllFavorites().forEach {
            updateCalculatedExchangeRate(
                it.favoriteCurrencyId,
                it.calculateExchangeRate(getFavoriteExchangeRate(mainCurrencySymbol)),
                mainCurrencySymbol
            )
        }
        return getAllFavorites()
    }

    @Transaction
    suspend fun updateAllAvailableCurrenciesWithCalculatedValues(mainCurrencySymbol: String): List<FavoriteCurrency> {
        getAvailableCurrencies().forEach {
            updateCalculatedExchangeRate(
                it.favoriteCurrencyId,
                it.calculateExchangeRate(getFavoriteExchangeRate(mainCurrencySymbol)),
                mainCurrencySymbol
            )
        }
        return getAvailableCurrencies()
    }

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