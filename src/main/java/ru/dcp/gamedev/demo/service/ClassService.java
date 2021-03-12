package ru.dcp.gamedev.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dcp.gamedev.demo.models.model.Classes;
import ru.dcp.gamedev.demo.repository.JpaClassRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ClassService {
    private final JpaClassRepository classRepository;

    public Iterable<Classes> getAllClasses() {
        return classRepository.findAll();
    }

    public Optional<Classes> getById(int id) { return classRepository.findById(id);}

}
