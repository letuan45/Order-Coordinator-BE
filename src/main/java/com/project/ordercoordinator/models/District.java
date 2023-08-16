package com.project.ordercoordinator.models;

import javax.persistence.*;

@Entity
@Table(name = "district")
public class District {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(targetEntity = Province.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "province_id")
    private Province province;

    public District() {
    }

    public District(Integer id, String name, Province province) {
        this.id = id;
        this.name = name;
        this.province = province;
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

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "District{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", province=" + province +
                '}';
    }
}
