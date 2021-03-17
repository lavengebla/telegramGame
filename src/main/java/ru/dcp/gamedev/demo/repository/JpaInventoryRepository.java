package ru.dcp.gamedev.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dcp.gamedev.demo.models.model.Characters;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.models.model.inventory.Inventory;
import ru.dcp.gamedev.demo.models.model.inventory.Item;


@Repository
@Transactional
public interface JpaInventoryRepository extends JpaRepository<Inventory, Integer> {
    Iterable<Inventory> findByCharacter(Characters character);
}
