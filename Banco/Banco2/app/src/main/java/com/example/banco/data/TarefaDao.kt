package com.example.banco.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface TarefaDao {

    @Insert
    suspend fun inserir(tarefa: Tarefa)

    @Update
    suspend fun atualizar(tarefa: Tarefa)

    @Delete
    suspend fun excluir(tarefa: Tarefa)

    @Query("SELECT * FROM tarefas")
    suspend fun listar(): List<Tarefa>

    @Query("SELECT * FROM tarefas WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Tarefa?
}