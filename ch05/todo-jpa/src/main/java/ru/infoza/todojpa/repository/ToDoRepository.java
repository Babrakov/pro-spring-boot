package ru.infoza.todojpa.repository;

import ru.infoza.todojpa.domain.ToDo;
import org.springframework.data.repository.CrudRepository;

public interface ToDoRepository extends CrudRepository<ToDo,String> {


}
