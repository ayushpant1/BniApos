package com.example.bniapos.database.doas

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bniapos.database.entities.SchemaParamTable

@Dao
interface SchemaParamDao {
    @Query("SELECT * FROM schema_param_table WHERE schemaId=:schemaId")
    fun getSchemaParamBySchemaId(schemaId: Int): SchemaParamTable

    @Query("SELECT * FROM schema_param_table")
    fun getAll(): List<SchemaParamTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(schemaParam: SchemaParamTable)


    @Query("DELETE FROM schema_param_table WHERE schemaId=:schemaId")
    fun delete(schemaId: Int)

}