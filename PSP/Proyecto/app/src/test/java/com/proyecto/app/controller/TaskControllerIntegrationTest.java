package com.proyecto.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.app.model.Task;
import com.proyecto.app.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoRepository taskRepository;

    @Test
    public void listarTasksTest() throws Exception {
        // Arrange
        Task task1 = new Task(null, "Tarea 1");
        Task task2 = new Task(null, "Tarea 2");
        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        // Act & Assert
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Tarea 1"))
                .andExpect(jsonPath("$[1].nombre").value("Tarea 2"));
    }

    @Test
    public void agregarTaskTest() throws Exception {
        // Arrange
        Long id = 1L; // Simulamos un ID generado por la base de datos
        Task nuevaTask = new Task(id, "Nueva tarea");
        when(taskRepository.save(nuevaTask)).thenReturn(nuevaTask);

        // Act & Assert
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevaTask)))
                .andExpect(status().isCreated())  // Cambiar isOk() por isCreated()
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Nueva tarea"))
                .andExpect(jsonPath("$.id").value(id)); // Aseguramos que el ID se devuelve correctamente
    }


    @Test
    public void actualizarTaskTest() throws Exception {
        // Arrange
        Long id = 1L;
        Task taskExistente = new Task(id, "Tarea existente");
        Task taskActualizada = new Task(id, "Tarea actualizada");
        when(taskRepository.findById(id)).thenReturn(Optional.of(taskExistente));
        when(taskRepository.save(taskExistente)).thenReturn(taskActualizada); // Se devuelve la tarea actualizada

        // Act & Assert
        mockMvc.perform(put("/tasks/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskActualizada)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Tarea actualizada"))
                .andExpect(jsonPath("$.id").value(id)); // Aseguramos que el ID no cambió
    }


    @Test
    public void eliminarTaskTest() throws Exception {
        // Arrange
        Long id = 1L;
        Task taskExistente = new Task(id, "Tarea a eliminar");
        when(taskRepository.findById(id)).thenReturn(Optional.of(taskExistente));

        // Act & Assert
        mockMvc.perform(delete("/tasks/{id}", id))
                .andExpect(status().isOk());
    }
}
