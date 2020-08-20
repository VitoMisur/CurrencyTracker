package com.vito.misur.currencytracker.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vito.misur.currencytracker.network.data.Currency

@Dao
interface SupportedCurrenciesDao {
    @Query("SELECT * FROM supported_currency ORDER BY name")
    fun getSupportedCurrencies(): LiveData<List<Currency>>

    @Query("SELECT * FROM supported_currency WHERE id = :currencyId")
    fun getCurrency(currencyId: Long): LiveData<Currency>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSupportedCurrency(currency: Currency): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<Currency>): List<Long>

    @Transaction
    suspend fun insertNewCurrencies(currencies: List<Currency>): List<Long> {
        deleteAll()
        return insertAll(currencies)
    }

    @Delete
    suspend fun deleteSupportedCurrency(currency: Currency)

    @Delete
    suspend fun deleteMultiple(currencies: List<Currency>)

    @Query("DELETE FROM supported_currency")
    fun deleteAll()

    @Query("UPDATE supported_currency SET is_main_currency = ${false}")
    suspend fun clearMainCurrency()

    @Query("UPDATE supported_currency SET is_main_currency = ${true} WHERE id = :currencyId")
    suspend fun selectMainCurrency(currencyId: Long)

    @Transaction
    suspend fun setAsMainCurrency(currencyId: Long) {
        clearMainCurrency()
        selectMainCurrency(currencyId)
    }
}