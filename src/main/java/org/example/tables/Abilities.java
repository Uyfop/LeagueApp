package org.example.tables;

import javax.persistence.*;

@Entity
@Table(name = "ABILITIES", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"AbilityName"})
})
public class Abilities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ChampName", referencedColumnName = "ChampName")
    private Champions championName;

    @Column(name = "AbilityName", unique = true)
    private String abilityName;

    @Column(name = "AbilityCost")
    private int abilityCost;

    @Column(name = "AbilityCooldown")
    private int abilityCD;

    @Column(name = "AbilityDesciption")
    private String abilityDescription;

    public Abilities() {
    }

    public void setAbilityName(String abilityName){
        this.abilityName = abilityName;
    }

    public String getAbilityName(){
        return abilityName;
    }

    public void setAbilityCost(int abilityCost) {
        this.abilityCost = abilityCost;
    }

    public int getAbilityCost(){
        return abilityCost;
    }

    public void setAbilityDescription(String abilityDescription) {
        this.abilityDescription = abilityDescription;
    }

    public String getAbilityDescription(){
        return abilityDescription;
    }

    public int getAbilityCD() {
        return abilityCD;
    }

    public void setAbilityCD(int cooldown) {
        this.abilityCD = cooldown;
    }

    public void setChampionName(Champions championName) {
        this.championName = championName;
    }

    public Champions getChampionName() {
        return championName;
    }
}
