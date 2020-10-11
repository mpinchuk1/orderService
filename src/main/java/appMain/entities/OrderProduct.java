package appMain.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class OrderProduct {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID productId;
    private UUID orderId;

    public OrderProduct() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "id=" + id +
                ", productId=" + productId +
                ", orderId=" + orderId +
                '}';
    }
}
