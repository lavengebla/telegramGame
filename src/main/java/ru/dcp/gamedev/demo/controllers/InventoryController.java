package ru.dcp.gamedev.demo.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.dcp.gamedev.demo.models.model.Classes;
import ru.dcp.gamedev.demo.models.model.ClassesModel;
import ru.dcp.gamedev.demo.models.model.inventory.Inventory;
import ru.dcp.gamedev.demo.models.model.inventory.InventoryModel;
import ru.dcp.gamedev.demo.repository.JpaCharacterRepository;
import ru.dcp.gamedev.demo.repository.JpaClassRepository;
import ru.dcp.gamedev.demo.repository.JpaInventoryRepository;
import ru.dcp.gamedev.demo.repository.JpaItemRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;

@Controller
@RequestMapping(path="/inventory")
@Slf4j
public class InventoryController {

    @Autowired
    JpaInventoryRepository inventoryRepository;

    @Autowired
    JpaCharacterRepository characterRepository;

    @Autowired
    JpaItemRepository itemRepository;

    @GetMapping()
    public @ResponseBody
    Iterable<Inventory> getAllClasses() {
        return inventoryRepository.findAll();
    }

    @PostMapping(path = "/add")
    public @ResponseBody
    Inventory addInventoryItem(@RequestBody InventoryModel inventoryModel) throws IOException {
        return inventoryRepository.save(new Inventory(itemRepository.findById(inventoryModel.getItem()).get(), characterRepository.findById(inventoryModel.getCharacter()).get(), inventoryModel.isActive()));
    }
}
