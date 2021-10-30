package com.example.bniapos.database.doas

import androidx.room.*
import com.example.bniapos.database.entities.ControlTable

@Dao
interface ControlDao {
    @Query("SELECT * FROM control_table")
    fun getAll(): List<ControlTable>?

    @Query("SELECT * FROM control_table WHERE data_set_key=:dataSet")
    fun getDataSet(dataSet: String): List<ControlTable>?

    @Query("SELECT * FROM control_table WHERE data_set_key=:dataSet AND reference_data_set_key=:value")
    fun getDataSetWithReferenceData(dataSet: String, value: String?): List<ControlTable>?

    @Insert
    fun insert(controlTable: ControlTable)


    @Delete
    fun delete(controlTable: ControlTable)

    @Update
    fun update(controlTable: ControlTable)

}