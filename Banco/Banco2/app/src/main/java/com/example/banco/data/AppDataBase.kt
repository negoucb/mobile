package com.example.banco.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Tarefa::class],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun tarefaDao(): TarefaDao
}