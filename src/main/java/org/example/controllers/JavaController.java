package org.example.controllers;

import com.example.javafilmoratekotlin.model.Genre;
import com.example.javafilmoratekotlin.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/test")
public class JavaController {

    @GetMapping("/get")
    @Operation(summary = "Возврат коллекции")
    public List<String> returnList(){
        return new ArrayList<>();
    }

    @PostMapping("/post")
    @Operation(summary = "Добавление CompositeClass")
    public void addCompositeObj(@Parameter(required = true) Genre genre){
    }

    @DeleteMapping("/delete")
    @Operation(description = "Удаление по id")
    public void deleteById(Integer id){
    }

    @PutMapping("/put")
    @Operation(description = "Обновление Composite Collection")
    public void updateCollection (Collection<Genre> collection){
    }






}
