package ru.dcp.gamedev.demo.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.dcp.gamedev.demo.models.model.Classes;
import ru.dcp.gamedev.demo.models.model.ClassesModel;
import ru.dcp.gamedev.demo.repository.JpaClassRepository;

import java.io.IOException;

@Controller
@RequestMapping(path="/classes")
@Slf4j
public class ClassesController {

    @Autowired
    JpaClassRepository classRepository;

    @GetMapping()
    public @ResponseBody
    Iterable<Classes> getAllClasses() {
        return classRepository.findAll();
    }

    @PostMapping(path = "/add")
    public @ResponseBody
    Classes addClass(@RequestBody ClassesModel classesModel) throws IOException {
        return classRepository.save(new Classes(classesModel.getName(),
                classesModel.getDescription(),
                classesModel.getBase_int(),
                classesModel.getBase_agi(),
                classesModel.getBase_str())) ;
    }
}
