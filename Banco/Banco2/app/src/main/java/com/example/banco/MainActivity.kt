
package com.example.banco

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.banco.data.AppDataBase
import com.example.banco.data.Tarefa
import com.example.banco.data.TarefaDao
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var tarefaDao: TarefaDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextTarefa = findViewById<EditText>(R.id.editTextTarefa)
        val buttonAdicionar = findViewById<Button>(R.id.buttonAdicionar)
        val buttonBuscar = findViewById<Button>(R.id.buttonBuscar)

        val editIdTarefa = findViewById<EditText>(R.id.editIdTarefa)
        val editNovoTitulo = findViewById<EditText>(R.id.editNovoTitulo)
        val buttonAtualizar = findViewById<Button>(R.id.buttonAtualizar)
        val buttonExcluir = findViewById<Button>(R.id.buttonExcluir)

        val listaResultado = findViewById<TextView>(R.id.listaResultado)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java,
            "app-database"
        ).build()

        tarefaDao = db.tarefaDao()

        buttonAdicionar.setOnClickListener {
            val titulo = editTextTarefa.text.toString().trim()

            if (titulo.isEmpty()) {
                Toast.makeText(this, "Digite uma tarefa", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    val tarefa = Tarefa(
                        titulo = titulo,
                        concluida = false
                    )

                    tarefaDao.inserir(tarefa)

                    editTextTarefa.text.clear()
                    listarTarefas(listaResultado)

                    Toast.makeText(
                        this@MainActivity,
                        "Tarefa adicionada!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        buttonBuscar.setOnClickListener {
            lifecycleScope.launch {
                listarTarefas(listaResultado)
            }
        }

        buttonAtualizar.setOnClickListener {
            val id = editIdTarefa.text.toString().toIntOrNull()
            val novoTitulo = editNovoTitulo.text.toString().trim()

            if (id == null || novoTitulo.isEmpty()) {
                Toast.makeText(
                    this,
                    "Informe o ID e o novo título",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                lifecycleScope.launch {
                    val tarefa = tarefaDao.buscarPorId(id)

                    if (tarefa != null) {
                        val tarefaAtualizada = tarefa.copy(
                            titulo = novoTitulo
                        )

                        tarefaDao.atualizar(tarefaAtualizada)

                        editIdTarefa.text.clear()
                        editNovoTitulo.text.clear()
                        listarTarefas(listaResultado)

                        Toast.makeText(
                            this@MainActivity,
                            "Tarefa atualizada!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Tarefa não encontrada",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        buttonExcluir.setOnClickListener {
            val id = editIdTarefa.text.toString().toIntOrNull()

            if (id == null) {
                Toast.makeText(
                    this,
                    "Informe o ID da tarefa",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                lifecycleScope.launch {
                    val tarefa = tarefaDao.buscarPorId(id)

                    if (tarefa != null) {
                        tarefaDao.excluir(tarefa)

                        editIdTarefa.text.clear()
                        listarTarefas(listaResultado)

                        Toast.makeText(
                            this@MainActivity,
                            "Tarefa excluída!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Tarefa não encontrada",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private suspend fun listarTarefas(resultado: TextView) {
        val tarefas = tarefaDao.listar()

        resultado.text = if (tarefas.isEmpty()) {
            "Nenhuma tarefa cadastrada."
        } else {
            tarefas.joinToString("\n") {
                "ID: ${it.id} - ${it.titulo}"
            }
        }
    }
}
