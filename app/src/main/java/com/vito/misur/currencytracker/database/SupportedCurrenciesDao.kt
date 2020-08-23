package com.vito.misur.currencytracker.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vito.misur.currencytracker.network.data.Currency

@Dao
interface SupportedCurrenciesDao {

    @Query("SELECT * FROM supported_currency ORDER BY symbol")
    fun getSupportedCurrenciesLiveData(): LiveData<List<Currency>>

    @Query("SELECT * FROM supported_currency ORDER BY symbol")
    fun getSupportedCurrencies(): List<Currency>

    @Query("SELECT * FROM supported_currency WHERE id = :currencyId")
    fun getCurrencyLiveData(currencyId: Long): LiveData<Currency>

    @Query("SELECT * FROM supported_currency WHERE is_main_currency = :isMainCurrency")
    fun getMainCurrencyLiveData(isMainCurrency: Boolean = true): LiveData<Currency?>

    @Query("SELECT * FROM supported_currency WHERE is_main_currency = :isMainCurrency")
    fun getMainCurrency(isMainCurrency: Boolean = true): Currency?

    @Query("SELECT symbol FROM supported_currency WHERE is_main_currency = :isMainCurrency")
    fun getMainCurrencySymbol(isMainCurrency: Boolean = true): String

    @Query("SELECT id FROM supported_currency WHERE symbol = :symbol")
    fun getMainCurrencyNewId(symbol: String): Long

    @Query("SELECT name FROM supported_currency WHERE symbol = :symbol")
    fun getCurrencyName(symbol: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupportedCurrency(currency: Currency): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<Currency>): List<Long>

    @Transaction
    suspend fun insertNewCurrencies(currencies: List<Currency>): List<Long> {
        val mainCurrencySymbol = getMainCurrencySymbol()
        deleteAll()
        val newCurrenciesList = insertAll(currencies)
        selectMainCurrency(getMainCurrencyNewId(mainCurrencySymbol))
        return newCurrenciesList
    }

    @Delete
    suspend fun deleteSupportedCurrency(currency: Currency)

    @Delete
    suspend fun deleteMultiple(currencies: List<Currency>)

    @Query("DELETE FROM supported_currency")
    fun deleteAll()

    @Query("UPDATE supported_currency SET is_main_currency = :isFavorite")
    suspend fun clearMainCurrency(isFavorite: Boolean = false)

    @Query("UPDATE supported_currency SET is_main_currency = :isFavorite WHERE id = :currencyId")
    suspend fun selectMainCurrency(currencyId: Long, isFavorite: Boolean = true)

    @Transaction
    suspend fun setAsMainCurrency(currencyId: Long): Currency? {
        clearMainCurrency()
        selectMainCurrency(currencyId)
        return getMainCurrency()
    }
}