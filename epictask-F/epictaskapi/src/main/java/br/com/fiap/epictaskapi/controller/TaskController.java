package br.com.fiap.epictaskapi.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.epictaskapi.model.Task;
import br.com.fiap.epictaskapi.service.TaskService;

@RestController
public class TaskController {

    @Autowired
    private TaskService service;
    private Optional<Task> byId;
    
    @GetMapping("/api/task")
    public List<Task> index(){
        return service.listAll();
    }

    @PostMapping("/api/task")
    public ResponseEntity<Task> create(@RequestBody @Valid Task task){
        service.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/api/task/{id}")
    public ResponseEntity<Task> show(@PathVariable Long id){
        return ResponseEntity.of(service.getById(id));
    }
    @DeleteMapping("/api/task/{id}")
    public ResponseEntity<Object> destroy(@PathVariable Long id) {

        Optional<Task> optional = service.getById(id);

        if(optional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/api/task/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody @Valid Task newTask) {

        Optional<Task> optional = service.getById(id);

        if(optional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        var task = optional.get();
        BeanUtils.copyProperties(newTask, task);
        task.setId(id);

        service.save(task);

        return ResponseEntity.ok(task);
    }
}
