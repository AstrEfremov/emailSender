package com.example.demo;


import org.apache.logging.log4j.message.Message;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("api/task")
public class TasksRestController {

    private final TaskRepo repo;
    private final MessageSource source;

    public TasksRestController(TaskRepo repo, MessageSource source) {
        this.repo = repo;
        this.source = source;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.repo.findAll());
    }
    @PostMapping
    public ResponseEntity<?> CreateNewTask(@RequestBody NewTaskPayload payload
    , UriComponentsBuilder uriComponentsBuilder, Locale locale){
        if (payload.details() == null || payload.details().isBlank())

        {
            final var message = this.source.getMessage("task.create.details.errors.not_set", new Object[0], locale);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorsRepresentation(List.of(message)));
        }
        else {
        var task = new Task(payload.details());
        this.repo.save(task);
        return ResponseEntity.created(uriComponentsBuilder.path("/api/task/{taskId}")
                        .build(Map.of("taskId", task.id())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(task);
        }
    }
}
