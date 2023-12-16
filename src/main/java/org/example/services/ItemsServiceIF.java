package org.example.services;

import org.example.tables.Items;
import java.util.List;
import java.util.Optional;

public interface ItemsServiceIF {
    Items GetItemByName(String itemName);
    List<Items> ListAllItems();
    Items saveItem(Items item);
    boolean deleteItemByName(String itemName);
    Optional<Items> updateItem(String itemName, Items updatedItem);

    boolean checkRegexItemName(Items item);
}
