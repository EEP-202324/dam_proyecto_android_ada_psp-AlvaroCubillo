package com.proyecto.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.app.model.Task;
import com.proyecto.app.repository.TodoRepository;

@Service
public class PersonaService {

    @Autowired
    private TodoRepository personaRepository;

    public Task guardarPersona(Task persona) {
        return personaRepository.save(persona);
    }
    

    public List<Task> obtenerTodasLasPersonas() {
        return personaRepository.findAll();
    }
    
    public Optional<Task> obtenerPersonaPorId(Long id) {
        return personaRepository.findById(id);
    }
    
    public Task actualizarPersona(Long id, Task persona) {
        Task personaExistente = personaRepository.findById(id).orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        personaExistente.setNombre(persona.getNombre());
        personaExistente.setPrimer_apellido(persona.getPrimer_apellido());
        personaExistente.setSegundo_apellido(persona.getSegundo_apellido());
        personaExistente.setEdad(persona.getEdad());
        return personaRepository.save(personaExistente);
    }
    
    public void eliminarPersona(Long id) {
        Task persona = personaRepository.findById(id).orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        personaRepository.delete(persona);
    }
}
