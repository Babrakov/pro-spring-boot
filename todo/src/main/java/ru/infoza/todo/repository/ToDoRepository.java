package ru.infoza.todo.repository;

import org.springframework.data.repository.CrudRepository;
import ru.infoza.todo.domain.ToDo;

public interface ToDoRepository extends CrudRepository<ToDo,String> {
}
