package com.vito.misur.currencytracker.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vito.misur.currencytracker.database.entity.Currency

@Dao
interface SupportedCurrenciesDao {

    /**
     * @return List of all supported currencies
     * for future use mainly in BottomDrawer main currency selection
     */
    @Query("SELECT * FROM supported_currency ORDER BY symbol")
    fun getSupportedCurrencies(): List<Currency>

    /**
     * @return base currency LiveData for fast and responsive UI updates
     * @param isMainCurrency is used as static value for query
     */
    @Query("SELECT * FROM supported_currency WHERE is_main_currency = :isMainCurrency")
    fun getMainCurrencyLiveData(isMainCurrency: Boolean = true): LiveData<Currency?>

    /**
     * @return base currency instance to be used it
     * @see setAsMainCurrency to select new main currency
     * @param isMainCurrency is used as static value for query
     */
    @Query("SELECT * FROM supported_currency WHERE is_main_currency = :isMainCurrency")
    fun getMainCurrency(isMainCurrency: Boolean = true): Currency?

    /**
     * Used mostly for API calls that requires Currency symbol
     * @param isMainCurrency used as static value for query
     * @return currency symbol (USD, CZK, etc...)
     */
    @Query("SELECT symbol FROM supported_currency WHERE is_main_currency = :isMainCurrency")
    fun getMainCurrencySymbol(isMainCurrency: Boolean = true): String

    /**
     * Used to restore saved base currency after updating the list of supported currencies
     * @param symbol used to specify currency
     * @return recorded currency id
     */
    @Query("SELECT id FROM supported_currency WHERE symbol = :symbol")
    fun getMainCurrencyNewId(symbol: String): Long

    /**
     * @since Free fixer.io API does not provide currency name in exchange rates request
     * it was the only way to save full currency name for
     * @see FavoriteCurrency
     * @return full currency name that is saved in
     * @see Currency
     * @param symbol is used for searching
     */
    @Query("SELECT name FROM supported_currency WHERE symbol = :symbol")
    fun getCurrencyName(symbol: String): String

    /**
     * @return List of inserted to database currency Ids
     * Used for
     * @see insertNewCurrencies transition with repopulating the database
     * @param currencies list of retrieved from server currencies to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<Currency>): List<Long>

    /**
     * @return List of inserted to database currency Ids
     * Used for repopulating the database with updated data
     * @param currencies list of retrieved from server currencies to be inserted
     */
    @Transaction
    suspend fun insertNewCurrencies(currencies: List<Currency>): List<Long> {
        val mainCurrencySymbol = getMainCurrencySymbol()
        deleteAll()
        val newCurrenciesList = insertAll(currencies)
        selectMainCurrency(getMainCurrencyNewId(mainCurrencySymbol))
        return newCurrenciesList
    }

    /**
     * Used in
     * @see insertNewCurrencies to delete all records from database for repopulating
     */
    @Query("DELETE FROM supported_currency")
    fun deleteAll()

    /**
     * Used in
     * @see setAsMainCurrency to clear main currency due to database repopulating
     * @param isFavorite is used as static value for query
     */
    @Query("UPDATE supported_currency SET is_main_currency = :isFavorite")
    suspend fun clearMainCurrency(isFavorite: Boolean = false)

    /**
     * Checks the currency taken via
     * @param currencyId as the main application currency
     * @see setAsMainCurrency
     * @param isFavorite is used as static value for query
     */
    @Query("UPDATE supported_currency SET is_main_currency = :isFavorite WHERE id = :currencyId")
    suspend fun selectMainCurrency(currencyId: Long, isFavorite: Boolean = true)

    /**
     * Clears active main currency
     * @see clearMainCurrency
     * Selects new main app currency provided by
     * @param currencyId
     * @return new main currency
     * @see setAsMainCurrency
     */
    @Transaction
    suspend fun setAsMainCurrency(currencyId: Long): Currency? {
        clearMainCurrency()
        selectMainCurrency(currencyId)
        return getMainCurrency()
    }
}