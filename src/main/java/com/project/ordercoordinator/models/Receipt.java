package com.project.ordercoordinator.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "receipt")
public class Receipt {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false)
    @DateTimeFormat(pattern = "dd/mm/yyyy")
    private Date date;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @ManyToOne(targetEntity = Supplier.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(targetEntity = Employee.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(targetEntity = Warehouse.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    public Receipt() {
    }

    public Receipt(Date date, Integer totalPrice, Supplier supplier, Employee employee, Warehouse warehouse) {
        this.date = date;
        this.totalPrice = totalPrice;
        this.supplier = supplier;
        this.employee = employee;
        this.warehouse = warehouse;
    }

    public Receipt(Integer id, Date date, Integer totalPrice, Supplier supplier, Employee employee, Warehouse warehouse) {
        this.id = id;
        this.date = date;
        this.totalPrice = totalPrice;
        this.supplier = supplier;
        this.employee = employee;
        this.warehouse = warehouse;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "id=" + id +
                ", date=" + date +
                ", totalPrice=" + totalPrice +
                ", supplier=" + supplier +
                ", employee=" + employee +
                ", warehouse=" + warehouse +
                '}';
    }
}
