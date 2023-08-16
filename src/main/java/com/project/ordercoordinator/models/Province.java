package com.project.ordercoordinator.models;

import javax.persistence.*;

@Entity
@Table(name = "province")
public class Province {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(targetEntity = Region.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "region_id")
    private Region region;

    public Province() {
    }

    public Province(Integer id, String name, Region region) {
        this.id = id;
        this.name = name;
        this.region = region;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "Province{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", region=" + region +
                '}';
    }
}
