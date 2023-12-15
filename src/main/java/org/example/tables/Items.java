package org.example.tables;

import org.hibernate.Session;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Items", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ItemName"})
})
public class Items {
    @Id
    @Column(name = "ItemName", unique = true)
    @NotBlank(message = "Item name cannot be blank")
    private String itemName;

    @ManyToMany(mappedBy = "items")
    private List<Builds> builds;

    @Column(name = "ItemFirstStat")
    private String itemFirstStat;

    @Column(name = "ItemSecondStat")
    private String itemSecondStat;

    @Column(name = "ItemThirdStat")
    private String itemThirdStat;


    public Items(){

    }

    public void setItemName(String name){
        this.itemName = name;
    }

    public void setItemFirstStat(String itemFirstStat) {
        this.itemFirstStat = itemFirstStat;
    }

    public void setItemSecondStat(String itemSecondStat) {
        this.itemSecondStat = itemSecondStat;
    }

    public void setItemThirdStat(String itemThirdStat) {
        this.itemThirdStat = itemThirdStat;
    }

    public String getItemFirstStat() {
        return itemFirstStat;
    }

    public String getItemSecondStat() {
        return itemSecondStat;
    }

    public String getItemThirdStat() {
        return itemThirdStat;
    }

    public String getItemName() {
        return itemName;
    }
    public static List<Items> createItemsForBuild(Session session) {
        List<Items> itemsList = new ArrayList<>();

        Items item1 = fetchOrCreateItemByName(session, "Item1");
        itemsList.add(item1);

        Items item2 = fetchOrCreateItemByName(session, "Item2");
        itemsList.add(item2);

        return itemsList;
    }
    public static Items fetchOrCreateItemByName(Session session, String itemName) {
        Items item = session.createQuery("FROM Items i WHERE i.itemName = :name", Items.class)
                .setParameter("name", itemName)
                .uniqueResult();

        if (item == null) {
            item = new Items();
            item.setItemName(itemName);
            session.save(item);
        }

        return item;
    }
}