package ru.dcp.gamedev.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dcp.gamedev.demo.models.model.Characters;
import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.models.model.inventory.Item;

import java.util.Optional;


@Repository
@Transactional
public interface JpaItemRepository extends JpaRepository<Item, Integer> {
    Optional<Item> findByItemType(Item.item_type item_type);
}
