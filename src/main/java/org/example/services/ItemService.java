package org.example.services;

import org.example.repositories.ItemsRepository;
import org.example.tables.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService implements ItemsServiceIF{

    private final ItemsRepository itemsRepository;
    @Autowired
    public ItemService(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }
    public Items GetItemByName(String itemName){
        return itemsRepository.findByItemName(itemName);
    }
    public List<Items> ListAllItems(){
        return itemsRepository.findAll();
    }
    public Items saveItem(Items item){
        Items existingItem = itemsRepository.findByItemName(item.getItemName());
        if(existingItem != null)
            throw new IllegalArgumentException("The item already in the database");
        if (checkRegexItemName(item)) {
            return itemsRepository.save(item);
        } else {
            throw new IllegalArgumentException("Invalid item name format");
        }
    }
    public boolean deleteItemByName(String itemName) {
        Items item = itemsRepository.findByItemName(itemName);
        if (item != null) {
            itemsRepository.delete(item);
            return true;
        } else {
            throw new IllegalArgumentException("Invalid item doesnt exist");
        }
    }

    public Optional<Items> updateItem(String itemName, Items updatedItem) {
        Optional<Items> optionalItem = Optional.ofNullable(itemsRepository.findByItemName(itemName));
        if (optionalItem.isPresent()) {
            Items item = optionalItem.get();
            if (!itemName.equals(updatedItem.getItemName())) {
                throw new IllegalArgumentException("Item name cannot be changed.");
            }
            item.setItemFirstStat(updatedItem.getItemFirstStat());
            item.setItemSecondStat(updatedItem.getItemSecondStat());
            item.setItemThirdStat(updatedItem.getItemThirdStat());
            return Optional.of(itemsRepository.save(item));
        } else {
            return Optional.empty();
        }
    }
    public boolean checkRegexItemName(Items item) {
        String itemName = item.getItemName();
        String regexPattern = "^[A-Za-z]+$";
        return itemName.matches(regexPattern);
    }

}
