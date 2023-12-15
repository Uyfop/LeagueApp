package org.example.tables;

import org.hibernate.Session;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "CHAMPIONS", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ChampName"})
})
public class Champions {
    @Id
    @Column(name = "ChampName", unique = true)
    @NotBlank(message = "Champion name cannot be blank")
    private String champName;

    @Column(name = "ChampType")
    private String champType;

    @OneToMany(mappedBy = "championName", cascade = CascadeType.ALL)
    private List<Abilities> abilities;

    @OneToOne(mappedBy = "champion")
    private Builds build;

    public Champions() {
    }

    public void setChampName(String name){

        this.champName = name;
    }

    public String getChampName() {

        return champName;
    }

    public void setChampType(String type){

        this.champType = type;
    }

    public String getChampType(){
        return champType;
    }

   public static Champions fetchChampionByName(Session session, String championName) {
        return session.createQuery("FROM Champions c WHERE c.champName = :name", Champions.class)
                .setParameter("name", championName)
                .uniqueResult();
    }
}