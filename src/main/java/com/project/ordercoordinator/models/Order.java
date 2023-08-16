package com.project.ordercoordinator.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false)
    @DateTimeFormat(pattern = "dd/mm/yyyy")
    private Date date;

    @ManyToOne(targetEntity = Partner.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @ManyToOne(targetEntity = Employee.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(targetEntity = Warehouse.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "warehouse_id")
    private Warehouse deliveryWarehouse;

    @ManyToOne(targetEntity = Customer.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(targetEntity = District.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "district_id")
    private District district;

    @Column(name = "addition_address")
    private String additionAddress;

    @Column(name = "delivery_price")
    private Integer deliveryPrice;

    @Column(name = "status")
    private Integer status; //1: Chờ xử lý, 2: Đã điều phối, 3: Hoàn tất, 4: Đã hủy

    public Order() {
        status = 1;
    }

    public Order(Date date, Employee employee, Customer customer, District district, String additionAddress) {
        this.date = date;
        this.employee = employee;
        this.customer = customer;
        this.district = district;
        this.additionAddress = additionAddress;
        this.status = 1;
    }

    public Order(Integer id, Date date, Partner partner, Employee employee, Warehouse deliveryWarehouse, Customer customer, District district, String additionAddress, Integer deliveryPrice, Integer status) {
        this.id = id;
        this.date = date;
        this.partner = partner;
        this.employee = employee;
        this.deliveryWarehouse = deliveryWarehouse;
        this.customer = customer;
        this.district = district;
        this.additionAddress = additionAddress;
        this.deliveryPrice = deliveryPrice;
        this.status = status;
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

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Warehouse getDeliveryWarehouse() {
        return deliveryWarehouse;
    }

    public void setDeliveryWarehouse(Warehouse deliveryWarehouse) {
        this.deliveryWarehouse = deliveryWarehouse;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getAdditionAddress() {
        return additionAddress;
    }

    public void setAdditionAddress(String additionAddress) {
        this.additionAddress = additionAddress;
    }

    public Integer getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Integer deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date=" + date +
                ", partner=" + partner +
                ", employee=" + employee +
                ", deliveryWarehouse=" + deliveryWarehouse +
                ", customer=" + customer +
                ", district=" + district +
                ", additionAddress='" + additionAddress + '\'' +
                ", deliveryPrice=" + deliveryPrice +
                ", status=" + status +
                '}';
    }
}
