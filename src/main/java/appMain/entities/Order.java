package appMain.entities;

import appMain.entities.dto.ProductDTO;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    private UUID id;
    private Double price;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "seller_id")
    private Seller seller;
    private UUID customerId;
    private Date orderDate;
    @Transient
    private final Calendar c = Calendar.getInstance();

    public Order() {
    }

    public Order(UUID id, Seller seller, UUID customerId, double price) {
        this.id = id;
        this.seller = seller;
        this.customerId = customerId;
        this.price = price;
        this.orderDate = c.getTime();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", price=" + price +
                ", seller=" + seller +
                ", customer=" + customerId +
                ", orderDate=" + orderDate +
                '}';
    }
}
