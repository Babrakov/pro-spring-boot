package ru.infoza.todojpa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.infoza.todojpa.domain.ToDo;
import ru.infoza.todojpa.domain.ToDoBuilder;
import ru.infoza.todojpa.repository.ToDoRepository;
import ru.infoza.todojpa.validation.ToDoValidationError;
import ru.infoza.todojpa.validation.ToDoValidationErrorBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ToDoController {
    private ToDoRepository repository;
    private static Logger log = LoggerFactory.getLogger(ToDoController.class);

    @Autowired
    public ToDoController(ToDoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/todo")
    public ResponseEntity<Iterable<ToDo>> getToDos(){
        return ResponseEntity
                .ok(repository.findAll());
    }

    @GetMapping("/todo/{id}")
    public  ResponseEntity<ToDo> getToDoById(@PathVariable String id){
        Optional<ToDo> toDo =
                repository.findById(id);
        if(toDo.isPresent())
            return ResponseEntity.ok(toDo.get());

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/todo/{id}")
    public  ResponseEntity<ToDo> setCompleted(@PathVariable String id){
        Optional<ToDo> toDo = repository.findById(id);
        if(!toDo.isPresent())
            return ResponseEntity.notFound().build();

        ToDo result = toDo.get();
        result.setCompleted(true);
        repository.save(result);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.ok().header("Location",location.toString()).build();
    }

    @RequestMapping(value = "/todo", method = {RequestMethod.POST, RequestMethod.PUT})
    public  ResponseEntity<?> createToDo(@Valid @RequestBody ToDo toDo, Errors errors) {
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach(e -> log.error(e.getObjectName() + " " + e.getDefaultMessage()));
            return ResponseEntity.badRequest().body(ToDoValidationErrorBuilder.fromBindingErrors(errors));
        }

        ToDo result = repository.save(toDo);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();
        return ResponseEntity
                .created(location)
                .build();
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable String id){
        repository.delete(ToDoBuilder.create().withId(id).build());
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/todo")
    public ResponseEntity<ToDo> deleteToDo(@RequestBody ToDo toDo){
        repository.delete(toDo);
        return ResponseEntity
                .noContent()
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ToDoValidationError handleException(Exception exception) {
        log.error(exception.getMessage());
        return new ToDoValidationError(exception.getMessage());
    }
}
