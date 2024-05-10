package com.proyecto.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.app.model.Task;
import com.proyecto.app.repository.TodoRepository;

@RestController
public class TodoController {
	@Autowired
	private TodoRepository todoRepository;
	
	
	@GetMapping(value="/tasks")
	public List <Task> getTasks(){
		return todoRepository.findAll();
		
	}
	
	@PostMapping(value="/savetask")
	public String saveTask(@RequestBody Task task) {
		todoRepository.save(task);
		return "Saved task";
	}
	
	@PutMapping(value="/update/{id}")
	public String updateTask(@PathVariable long id, @RequestBody Task task) {
		Task updatedTask = todoRepository.findById(id).get();
		updatedTask.setNombre(task.getNombre());
		updatedTask.setPrimer_apellido(task.getPrimer_apellido());
		updatedTask.setSegundo_apellido(task.getSegundo_apellido());
		updatedTask.setEdad(task.getEdad());
		todoRepository.save(updatedTask);
		return "Updated Task";
		
	}
	
	@DeleteMapping(value="delete/{id}")
	public String deleteTask(@PathVariable long id) {
		Task deletedTask = todoRepository.findById(id).get();
		todoRepository.delete(deletedTask);
		return "Deleted Task";
	}
}
