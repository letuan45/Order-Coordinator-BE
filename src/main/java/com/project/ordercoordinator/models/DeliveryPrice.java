package com.project.ordercoordinator.models;

import javax.persistence.*;

@Entity
@Table(name = "delivery_price")
public class DeliveryPrice {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(targetEntity = Partner.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @ManyToOne(targetEntity = DeliveryType.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_type_id")
    private DeliveryType deliveryType;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "price", nullable = false)
    private Integer price;

    public DeliveryPrice() {
        this.isActive = true;
    }

    public DeliveryPrice(Integer id, Partner partner, DeliveryType deliveryType, Boolean isActive, Integer price) {
        this.id = id;
        this.partner = partner;
        this.deliveryType = deliveryType;
        this.isActive = isActive;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public DeliveryType getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "DeliveryPrice{" +
                "id=" + id +
                ", partner=" + partner +
                ", deliveryType=" + deliveryType +
                ", isActive=" + isActive +
                ", price=" + price +
                '}';
    }
}
