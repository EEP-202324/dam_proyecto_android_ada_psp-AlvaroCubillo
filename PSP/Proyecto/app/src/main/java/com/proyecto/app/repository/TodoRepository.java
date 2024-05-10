package com.proyecto.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.app.model.Task;

public interface TodoRepository extends JpaRepository <Task, Long>{

}
