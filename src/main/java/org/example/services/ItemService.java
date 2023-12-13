package org.example.services;

import org.example.repositories.ChampionsRepository;
import org.example.repositories.ItemsRepository;
import org.example.tables.Champions;
import org.example.tables.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
