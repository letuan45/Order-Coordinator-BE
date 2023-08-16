package com.project.ordercoordinator.models;

import com.project.ordercoordinator.keys.StockId;

import javax.persistence.*;

@Entity
@Table(name = "stock")
public class Stock {
//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @EmbeddedId
    private StockId stockId;

//    @ManyToOne(targetEntity = Warehouse.class, cascade = CascadeType.ALL)
//    @JoinColumn(name = "warehouse_id")
//    private Warehouse warehouse;
//
//    @ManyToOne(targetEntity = Product.class, cascade = CascadeType.ALL)
//    @JoinColumn(name = "product_id")
//    private Product product;

    public Stock() {
        this.isActive = true;
    }

    public Stock(Integer quantity, StockId stockId) {
        this.isActive = true;
        this.quantity = quantity;
        this.stockId = stockId;
    }

    public Stock(Integer quantity, Boolean isActive, StockId stockId) {
        this.quantity = quantity;
        this.isActive = isActive;
        this.stockId = stockId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public StockId getStockId() {
        return stockId;
    }

    public void setStockId(StockId stockId) {
        this.stockId = stockId;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "quantity=" + quantity +
                ", isActive=" + isActive +
                ", stockId=" + stockId +
                '}';
    }
}
