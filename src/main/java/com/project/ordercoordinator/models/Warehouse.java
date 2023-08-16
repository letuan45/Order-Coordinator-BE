package com.project.ordercoordinator.models;

import javax.persistence.*;

@Entity
@Table(name = "warehouse")
public class Warehouse {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "addition_address", nullable = false)
    private String additionAddress;

    @ManyToOne(targetEntity = District.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "district_id")
    private District district;

    public Warehouse() {
        this.isActive = true;
    }

    public Warehouse(Integer id, String name, Boolean isActive, String additionAddress, District district) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
        this.additionAddress = additionAddress;
        this.district = district;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getAdditionAddress() {
        return additionAddress;
    }

    public void setAdditionAddress(String additionAddress) {
        this.additionAddress = additionAddress;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                ", additionAddress='" + additionAddress + '\'' +
                ", district=" + district +
                '}';
    }
}
