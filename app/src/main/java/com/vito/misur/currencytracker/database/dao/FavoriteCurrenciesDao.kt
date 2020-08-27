package com.vito.misur.currencytracker.database.dao

import androidx.room.*
import com.vito.misur.currencytracker.database.entity.FavoriteCurrency

/**
 * Favorites Dao for Database interactions
 * Most used parts were overwritten to properly function with FREE fixer.io API
 * With correct API it would've been much prettier
 */
@Dao
interface FavoriteCurrenciesDao {

    /**
     * @return List of ALL available currencies
     * @since Free fixer.io API has been used, base currency was removed
     *
     */
    @Query("SELECT * FROM favorite_currencies ORDER BY symbol")
    fun getAvailableCurrencies(): List<FavoriteCurrency>

    /**
     * @return List of ALL available currencies filtered by specific
     * @param searchQuery
     * @since Free fixer.io API has been used, base currency was removed
     */
    @Query("SELECT * FROM favorite_currencies WHERE symbol OR name LIKE :searchQuery ORDER BY symbol")
    fun getAvailableCurrenciesFiltered(
        searchQuery: String
    ): List<FavoriteCurrency>

    /**
     * @param isFavorite is used as static value for query
     * @return List of all favorite Currencies
     */
    @Query("SELECT * FROM favorite_currencies WHERE is_favorite = :isFavorite ORDER BY symbol")
    fun getAllFavorites(
        isFavorite: Boolean = true
    ): List<FavoriteCurrency>

    /**
     * @param isFavorite is used as static value for query
     * @return list of Currency symbols (EUR, USD, etc..) for future use instead of IDs
     */
    @Query("SELECT symbol FROM favorite_currencies WHERE is_favorite = :isFavorite")
    fun getAllFavoritesSymbols(isFavorite: Boolean = true): List<String>

    /**
     * @return exchange rate of specified
     * @param currencySymbol
     * @since use for calculation purposes due to Free fixer.io API
     */
    @Query("SELECT exchange_rate FROM favorite_currencies WHERE symbol = :currencySymbol")
    fun getFavoriteExchangeRate(currencySymbol: String): Double

    /**
     * Restores
     * @param isFavorite is used as static value for query
     * previously saved/favorite currencies via
     * @param symbols list taken from database
     * symbols used due to concerns regarding the ID's
     */
    @Query("UPDATE favorite_currencies SET is_favorite = :isFavorite WHERE symbol IN (:symbols)")
    fun restoreFavorites(symbols: List<String>, isFavorite: Boolean = true)

    /**
     * @return number of updated rows
     * Checks the currency taken via
     * @param currencyId
     * as Favorite
     */
    @Query("UPDATE favorite_currencies SET is_favorite = :isFavorite WHERE id = :currencyId")
    fun setCurrencyAsFavorite(currencyId: Long, isFavorite: Boolean): Int

    /**
     * @return List of inserted to database currency Ids
     * Used for
     * @see repopulateFavorites transition with repopulating the database
     * @param availableCurrencies list of retrieved from server currencies to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAvailableCurrenciesList(availableCurrencies: List<FavoriteCurrency>): List<Long>

    /**
     * @since Free fixer.io API forced me to calculate exchange rates
     * @return List of updated rows
     * @see updateFavoriteCurrenciesWithCalculatedValues and
     * @see updateAllAvailableCurrenciesWithCalculatedValues
     * @param id currency database identifier
     * @param calculatedExchangeRate exchange rate calculated for specified
     * @param baseCurrency
     */
    @Query("UPDATE favorite_currencies SET calculated_exchange_rate = :calculatedExchangeRate, base_currency = :baseCurrency WHERE id = :id")
    fun updateCalculatedExchangeRate(
        id: Long,
        calculatedExchangeRate: Double,
        baseCurrency: String
    ): Int

    /**
     * @since Free fixer.io API forced me to calculate exchange rates
     * @return List of Favorite only currencies
     * @param mainCurrencySymbol used for
     * @see getFavoriteExchangeRate to retrieve the exchange rate value for calculating exchange rates in favorite currencies
     */
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

    /**
     * @since Free fixer.io API forced me to calculate exchange rates
     * @return List of ALL Available for selecting currencies
     * @param mainCurrencySymbol used for
     * @see getFavoriteExchangeRate to retrieve the exchange rate value for calculating exchange rates in all provided currencies
     */
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

    /**
     * Used in
     * @see repopulateFavorites to delete all currency records from database
     */
    @Query("DELETE FROM favorite_currencies")
    fun deleteAll()

    /**
     * @param availableCurrencies used to retrieve list of all recorded currencies for future recreation with updated data from server
     */
    @Transaction
    suspend fun repopulateFavorites(availableCurrencies: List<FavoriteCurrency>) {
        val favorites = getAllFavoritesSymbols()
        deleteAll()
        insertAvailableCurrenciesList(availableCurrencies)
        restoreFavorites(favorites)
    }
}