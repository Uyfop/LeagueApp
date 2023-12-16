package org.example.services;

import org.example.repositories.ChampionsRepository;
import org.example.repositories.ItemsRepository;
import org.example.tables.Champions;
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
        itemsRepository.save(item);
        return item;
    }
    public boolean deleteItemByName(String itemName){
        Items item = itemsRepository.getReferenceById(itemName);
        if(item != null) {
            itemsRepository.delete(item);
            return true;
        }
        return false;
    }

    public Optional<Items> updateItem(String itemName, Items updatedItem) {
        Optional<Items> optionalItem = Optional.ofNullable(itemsRepository.findByItemName(itemName));
        if (optionalItem.isPresent()) {
            Items item = optionalItem.get();
            item.setItemFirstStat(updatedItem.getItemFirstStat());
            item.setItemSecondStat(updatedItem.getItemSecondStat());
            item.setItemThirdStat(updatedItem.getItemThirdStat());
            return Optional.of(itemsRepository.save(item));
        } else {
            return Optional.empty();
        }
    }

}
