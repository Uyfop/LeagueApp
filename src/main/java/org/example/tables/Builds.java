package org.example.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.Session;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Builds")
public class Builds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ChampName", referencedColumnName = "ChampName")
    private Champions champion;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "Build_Items",
            joinColumns = @JoinColumn(name = "BuildID"),
            inverseJoinColumns = @JoinColumn(name = "ItemName")
    )
    private List<Items> items = new ArrayList<>();

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;



    public void setChampion(Champions champion) {
        this.champion = champion;
    }

    public Champions getChampion() {
        return champion;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public List<Items> getItems() {
        return items;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }


    public Long getId() {
        return id;
    }

}
