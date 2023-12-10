package org.example.tablesImport;

import org.example.tables.Abilities;
import org.example.tables.Builds;
import org.example.tables.Champions;
import org.example.tables.Items;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.awt.*;
import java.io.*;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.example.tables.Champions.fetchChampionByName;
import static org.example.tables.Items.createItemsForBuild;


public class CreateModel {

    static EntityManager entityManager = null;
    static EntityManagerFactory entityManagerFactory = null;

    public static void Main() {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-dynamic");
            entityManager = entityManagerFactory.createEntityManager();
            Session session = entityManager.unwrap(Session.class);
            session.beginTransaction();
            importChampions(session);
            importItems(session);
            importAbilities(session);
            session.getTransaction().commit();
            session.close();
            entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-dynamic");
            entityManager = entityManagerFactory.createEntityManager();
            Session session1 = entityManager.unwrap(Session.class);
            session1.beginTransaction();
            importBuilds(session1);

            session1.getTransaction().commit();
            session1.close();
        } catch (HibernateException e) {
            System.err.println("Initial SessionFactory creation failed.");
        }
    }
    private static void importChampions(Session session) {
        ImportData importData = new ImportData();
        importData.importEntities(session, "/ChampionData.txt", new EntityCreator() {
            @Override
            public Map<String, Object> createAttributes(String[] fieldData) {
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("name", fieldData[0]);
                attributes.put("type", fieldData[1]);
                return attributes;
            }

            @Override
            public Object createEntity(Map<String, Object> attributes) {
                Champions champion = new Champions();
                champion.setChampName((String) attributes.get("name"));
                champion.setChampType((String) attributes.get("type"));
                Champions championcheck = session.createQuery("FROM Champions C WHERE C.champName = :name", Champions.class)
                        .setParameter("name", champion.getChampName())
                        .uniqueResult();
                if(championcheck == null) {
                    return champion;
                }

                return null;
            }
        });
    }

    private static void importItems(Session session) {
        ImportData importData = new ImportData();
        importData.importEntities(session, "/ItemsData.txt", new EntityCreator() {
            @Override
            public Map<String, Object> createAttributes(String[] fieldData) {
                Map<String, Object> attributes = new HashMap<>();
                if (fieldData.length >= 2) {
                    attributes.put("name", fieldData[0]);
                    attributes.put("stat1", fieldData[1]);
                }
                if(fieldData.length >= 3){
                    attributes.put("stat2", fieldData[2]);
                }
                if(fieldData.length == 4)
                    attributes.put("stat3", fieldData[3]);
                return attributes;
            }

            @Override
            public Object createEntity(Map<String, Object> attributes) {
                Items item = new Items();
                item.setItemName((String) attributes.get("name"));
                item.setItemFirstStat((String) attributes.get("stat1"));
                item.setItemSecondStat((String) attributes.get("stat2"));
                item.setItemThirdStat((String) attributes.get("stat3"));
                Items itemcheck = session.createQuery("FROM Items i WHERE i.itemName = :name", Items.class)
                        .setParameter("name", item.getItemName())
                        .uniqueResult();
                if(itemcheck == null) {
                    return item;
                }

                return null;
            }
        });
    }
    private static void importBuilds(Session session) {
        ImportData importData = new ImportData();
        importData.importEntities(session, "/BuildsData.txt", new EntityCreator() {
            @Override
            public Map<String, Object> createAttributes(String[] fieldData) {
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("champname", fieldData[0]);
                Builds build = new Builds();
                List<Items> itemsList = session.createQuery("FROM Items", Items.class).list();
                for (int i = 1; i < fieldData.length; i++) {
                    String itemName = fieldData[i];
                    Items item = itemsList.stream()
                            .filter(it -> it.getItemName().equals(itemName))
                            .findFirst()
                            .orElse(null);
                    if (item != null) {
                        build.getItems().add(item);
                    }
                }
                attributes.put("build", build);
                return attributes;
            }

            @Override
            public Object createEntity(Map<String, Object> attributes) {
                Builds build = (Builds) attributes.get("build");
                Champions champion = fetchChampionByName(session, (String) attributes.get("champname"));
                build.setChampion(champion);
                ZonedDateTime now = ZonedDateTime.now();
                build.setCreationDate(now);
                Builds buildcheck = session.createQuery("FROM Builds b WHERE b.champion = :name", Builds.class)
                        .setParameter("name", champion)
                        .uniqueResult();
                if (champion != null && buildcheck == null) {
                    session.save(build);
                    return build;
                }
                return null;
            }
        });
    }
    private static void importAbilities(Session session) {
        ImportData importData = new ImportData();
        importData.importEntities(session, "/AbilitiesData.txt", new EntityCreator() {
            @Override
            public Map<String, Object> createAttributes(String[] fieldData) {
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("champname", fieldData[0]);
                attributes.put("abilityname", fieldData[1]);
                attributes.put("abilitycost", fieldData[2]);
                attributes.put("abilitycd",fieldData[3]);
                attributes.put("abilitydescription",fieldData[4]);
                return attributes;
            }

            @Override
            public Object createEntity(Map<String, Object> attributes) {
                Abilities ability = new Abilities();
                Champions champion = fetchChampionByName(session, (String) attributes.get("champname"));
                ability.setChampionName(champion);
                ability.setAbilityName((String) attributes.get("abilityname"));
                ability.setAbilityCost(Integer.parseInt((String)attributes.get("abilitycost")));
                ability.setAbilityCD(Integer.parseInt((String) attributes.get("abilitycd")));
                ability.setAbilityDescription((String) attributes.get("abilitydescription"));
                Abilities abilitytest = session.createQuery("FROM Abilities a WHERE a.abilityName = :name", Abilities.class)
                        .setParameter("name", attributes.get("abilityname"))
                        .uniqueResult();
                if (abilitytest == null) {
                    session.save(ability);
                    return ability;
                }
                return null;
            }
        });
    }


}
