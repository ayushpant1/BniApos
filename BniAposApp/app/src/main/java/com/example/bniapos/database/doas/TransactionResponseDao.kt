package com.example.bniapos.database.doas

import androidx.room.*
import com.example.bniapos.database.entities.TransactionResponseTable

@Dao
interface TransactionResponseDao {
    @Query("SELECT * FROM transaction_response_table")
    fun getAll(): List<TransactionResponseTable>?

    @Insert
    fun insert(controlTable: TransactionResponseTable)


    @Delete
    fun delete(controlTable: TransactionResponseTable)

    @Update
    fun update(controlTable: TransactionResponseTable)

}