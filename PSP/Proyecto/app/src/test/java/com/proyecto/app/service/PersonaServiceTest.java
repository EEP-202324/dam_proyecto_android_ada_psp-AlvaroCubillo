package com.proyecto.app.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.proyecto.app.model.Task;
import com.proyecto.app.repository.TodoRepository;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonaServiceTest {

    @Mock
    private TodoRepository personaRepository;

    @InjectMocks
    private PersonaService personaService;

    public PersonaServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void guardarPersonaTest() {
        // Arrange
        Task persona = new Task();
        persona.setNombre("John");
        persona.setPrimer_apellido("Doe");
        persona.setSegundo_apellido("Smith");
        persona.setEdad(30);

        // Act
        when(personaRepository.save(persona)).thenReturn(persona);
        Task savedPersona = personaService.guardarPersona(persona);

        // Assert
        assertEquals(persona, savedPersona);
    }

    @Test
    public void actualizarPersonaTest() {
        // Arrange
        Long id = 1L;
        Task personaExistente = new Task();
        personaExistente.setId(id);
        personaExistente.setNombre("John");
        personaExistente.setPrimer_apellido("Doe");
        personaExistente.setSegundo_apellido("Smith");
        personaExistente.setEdad(30);

        Task personaNueva = new Task();
        personaNueva.setNombre("Jane");
        personaNueva.setPrimer_apellido("Doe");
        personaNueva.setSegundo_apellido("Smith");
        personaNueva.setEdad(35);

        // Act
        when(personaRepository.findById(id)).thenReturn(Optional.of(personaExistente));
        when(personaRepository.save(personaExistente)).thenReturn(personaExistente);
        Task updatedPersona = personaService.actualizarPersona(id, personaNueva);

        // Assert
        assertEquals(personaNueva.getNombre(), updatedPersona.getNombre());
        assertEquals(personaNueva.getEdad(), updatedPersona.getEdad());
    }

    @Test
    public void eliminarPersonaTest() {
        // Arrange
        Long id = 1L;
        Task persona = new Task();
        persona.setId(id);
        persona.setNombre("John");
        persona.setPrimer_apellido("Doe");
        persona.setSegundo_apellido("Smith");
        persona.setEdad(30);

        // Act
        when(personaRepository.findById(id)).thenReturn(Optional.of(persona));
        personaService.eliminarPersona(id);

        // Assert
        verify(personaRepository, times(1)).delete(persona);
    }
}
