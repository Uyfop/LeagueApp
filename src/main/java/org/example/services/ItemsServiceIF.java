package org.example.services;

import org.example.tables.Items;
import java.util.List;
public interface ItemsServiceIF {
    Items GetItemByName(String itemName);
    List<Items> ListAllItems();
    Items saveItem(Items item);
    boolean deleteItemByName(String itemName);
}
