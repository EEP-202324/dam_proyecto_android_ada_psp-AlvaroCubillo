package com.example.aplicacion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion.model.Task
import com.example.aplicacion.network.RetrofitApiService
import com.example.aplicacion.ui.theme.AplicacionTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: TaskViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: TaskViewModel by viewModels()

            AplicacionTheme {
                Surface(color = MaterialTheme.colors.background) {
                    TaskScreen(viewModel = viewModel)
                }
            }
        }
    }

    fun onTaskSelected(task: Task) {
        // Implementación de la función
    }
}


@Composable
fun DetailScreen(task: Task) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Detalles de la tarea", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        Text("ID: ${task.id}", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Nombre: ${task.nombre}", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Primer Apellido: ${task.primer_apellido}", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Segundo Apellido: ${task.segundo_apellido}", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Edad: ${task.edad}", style = MaterialTheme.typography.body1)
    }
}

@Composable
fun TaskScreen(viewModel: TaskViewModel, selectedTask: Task? = null) {
    val tasks by viewModel.tasks.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var primerApellido by remember { mutableStateOf("") }
    var segundoApellido by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getTasks()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TaskList(
            tasks = tasks,
            onUpdate = { task -> viewModel.updateTask(task) },
            onDelete = { id -> viewModel.deleteTask(id) },
            onTaskSelected = { task -> viewModel.setSelectedTask(task) },
                    viewModel = viewModel
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = primerApellido,
            onValueChange = { primerApellido = it },
            label = { Text("Primer Apellido") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = segundoApellido,
            onValueChange = { segundoApellido = it },
            label = { Text("Segundo Apellido") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = edad,
            onValueChange = { edad = it },
            label = { Text("Edad") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            viewModel.addTask(
                Task(
                    id = 0,
                    nombre = nombre,
                    primer_apellido = primerApellido,
                    segundo_apellido = segundoApellido,
                    edad = edad.toIntOrNull() ?: 0
                )
            )
            nombre = ""
            primerApellido = ""
            segundoApellido = ""
            edad = ""
        }) {
            Text("Agregar Tarea")
        }
    }

    selectedTask?.let { task ->
        DetailScreen(task)
    }
}





@Composable
fun TaskItem(
    task: Task,
    onUpdate: (Task) -> Unit,
    onDelete: (Long) -> Unit,
    onTaskSelected: (Task) -> Unit,
    viewModel: TaskViewModel
) {
    var nombre by remember { mutableStateOf(task.nombre ?: "") }
    var primerApellido by remember { mutableStateOf(task.primer_apellido ?: "") }
    var segundoApellido by remember { mutableStateOf(task.segundo_apellido ?: "") }
    var edad by remember { mutableStateOf(task.edad?.toString() ?: "") }

    Column(modifier = Modifier.clickable { onTaskSelected(task) }.padding(vertical = 8.dp)) {
        Text("ID: ${task.id}")
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") }
        )
        OutlinedTextField(
            value = primerApellido,
            onValueChange = { primerApellido = it },
            label = { Text("Primer Apellido") }
        )
        OutlinedTextField(
            value = segundoApellido,
            onValueChange = { segundoApellido = it },
            label = { Text("Segundo Apellido") }
        )
        OutlinedTextField(
            value = edad,
            onValueChange = { edad = it },
            label = { Text("Edad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(onClick = {
                viewModel.addTask(
                    Task(
                        id = 0,
                        nombre = nombre,
                        primer_apellido = primerApellido,
                        segundo_apellido = segundoApellido,
                        edad = edad.toIntOrNull() ?: 0
                    )
                )
                nombre = ""
                primerApellido = ""
                segundoApellido = ""
                edad = ""
            }) {
                Text("Agregar Tarea")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                val updatedTask = task.copy(
                    nombre = nombre,
                    primer_apellido = primerApellido,
                    segundo_apellido = segundoApellido,
                    edad = edad.toIntOrNull() ?: 0
                )
                onUpdate(updatedTask)
            }) {
                Text("Actualizar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onDelete(task.id) }) {
                Text("Eliminar")
            }
        }
    }
}

@Composable
fun TaskList(
    tasks: List<Task>,
    onUpdate: (Task) -> Unit,
    onDelete: (Long) -> Unit,
    onTaskSelected: (Task) -> Unit,
    viewModel: TaskViewModel // Agregar viewModel como parámetro
) {
    LazyColumn {
        items(tasks) { task ->
            TaskItem(
                task = task,
                onUpdate = onUpdate,
                onDelete = onDelete,
                onTaskSelected = onTaskSelected,
                viewModel = viewModel // Pasar viewModel como argumento
            )
        }
    }
}



class TaskViewModel : ViewModel() {
    private val apiService = RetrofitApiService()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    // Definir selectedTask como MutableStateFlow
    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask

    // Función para establecer la tarea seleccionada
    fun setSelectedTask(task: Task?) {
        _selectedTask.value = task
    }

    // Función para obtener la lista de tareas desde el servicio
    fun getTasks() {
        viewModelScope.launch {
            try {
                val tasksFromApi = apiService.getTasks()
                _tasks.value = tasksFromApi
            } catch (e: Exception) {
                // Manejo de errores
                e.printStackTrace()
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                // Llamar al servicio para agregar la tarea
                val newTask = apiService.createTask(task)
                // Actualizar la lista de tareas con la nueva tarea
                if (newTask != null) {
                    _tasks.value = (_tasks.value ?: emptyList()) + newTask
                }
            } catch (e: Exception) {
                // Manejo de errores
                e.printStackTrace()
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                val updatedTask = apiService.updateTask(task.id, task)
                if (updatedTask != null) {
                    _tasks.value = _tasks.value.map { if (it.id == task.id) updatedTask else it }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            try {
                val success = apiService.deleteTask(id)
                if (success) {
                    _tasks.value = _tasks.value.filter { it.id != id }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}