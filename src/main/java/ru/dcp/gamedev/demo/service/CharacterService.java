package ru.dcp.gamedev.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dcp.gamedev.demo.models.model.Characters;

import ru.dcp.gamedev.demo.models.model.User;
import ru.dcp.gamedev.demo.models.model.inventory.Inventory;
import ru.dcp.gamedev.demo.models.model.inventory.Item;
import ru.dcp.gamedev.demo.repository.JpaCharacterRepository;
import ru.dcp.gamedev.demo.repository.JpaInventoryRepository;
import ru.dcp.gamedev.demo.repository.JpaItemRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CharacterService {
    private final JpaCharacterRepository characterRepository;
    private final JpaItemRepository itemRepository;
    private final JpaInventoryRepository inventoryRepository;

    //работа с чаректарами

    public Iterable<Characters> getAllCharacters() {
        return characterRepository.findAll();
    }

    public Iterable<Characters> getMyCharacters(User user) { return  characterRepository.findAllByUser(user);}

    public Optional<Characters> getById(int id) { return characterRepository.findById(id);}

    public Characters create(User user, String name, int base_int, int base_agi, int base_str) {
        return characterRepository.save(new Characters(user, name, base_int, base_agi, base_str));
    }

    public void updateCharacter(Characters characters) {
        characterRepository.save(characters);
    }

    //работа с итемами

    public Item getOrCreate(Item.item_type item_type, int base_cost) {

        return get(item_type)
                .orElseGet(() -> itemRepository.save(new Item(item_type, base_cost)));
    }

    public Optional<Item> get(Item.item_type item_type) {return itemRepository.findByItemType(item_type);}

    //работа с инвентарем

    public Iterable<Inventory> getInventory(Characters character) {
        return inventoryRepository.findByCharacter(character);
    }

    public Inventory updateInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public void deleteInventoryEntity(Inventory inventory) {
        inventoryRepository.delete(inventory);
    }

}
