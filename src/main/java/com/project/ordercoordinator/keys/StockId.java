package com.project.ordercoordinator.keys;

import com.project.ordercoordinator.models.Product;
import com.project.ordercoordinator.models.Warehouse;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class StockId implements Serializable {
    @ManyToOne(targetEntity = Warehouse.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(targetEntity = Product.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    public StockId() {
    }

    public StockId(Warehouse warehouse, Product product) {
        this.warehouse = warehouse;
        this.product = product;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "StockId{" +
                "warehouse=" + warehouse +
                ", product=" + product +
                '}';
    }
}
