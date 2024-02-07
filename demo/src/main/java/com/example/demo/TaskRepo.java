package com.example.demo;

import java.util.List;

public interface TaskRepo {

    List<Task> findAll();

    void save(Task task);
}
