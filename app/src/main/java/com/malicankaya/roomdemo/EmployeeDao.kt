package com.malicankaya.roomdemo

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    @Insert
    suspend fun insert(employeeEntity: EmployeeEntity)

    @Update
    suspend fun update(employeeEntity: EmployeeEntity)

    @Delete
    suspend fun delete(employeeEntity: EmployeeEntity)

    @Query("select * from `employee-table`")
    fun fetchAllEmployees():Flow<List<EmployeeEntity>>

    @Query("select * from `employee-table` where id = :id")
    fun fetchEmployeeById(id: Int):Flow<EmployeeEntity>
}