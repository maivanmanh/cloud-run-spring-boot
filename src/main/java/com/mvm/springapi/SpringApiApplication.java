package com.mvm.springapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SpringBootApplication
@RestController
public class SpringApiApplication {

    private StudentRepo db;

    public SpringApiApplication(@Autowired StudentRepo db) {
        this.db = db;
    }


    @GetMapping("/")
    public List<Student> getAll() {
        return StreamSupport.stream(db.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable("id") int id) {
        return db.findById(id).orElse(null);
    }

    @PostMapping("/")
    public Student add(@RequestBody Student s) {
        db.save(s);
        return s;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") int id, @RequestBody Student student) {
        Optional<Student> st = db.findById(id);
        if (st.isPresent()) {
            Student x = st.get();
            x.clone(student);
            db.save(x);
            return ResponseEntity.ok("Updated v2");
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletById(@PathVariable("id") int id) {
        Optional<Student> st = db.findById(id);
        if (st.isPresent()) {
            db.deleteById(id);
            return ResponseEntity.ok("Deleted");
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringApiApplication.class, args);
    }

}
