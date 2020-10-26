package appMain.entities.dto;

import appMain.entities.Seller;

import java.util.List;

public class CreateOrderDTO {
    private Seller seller;
    private Customer customerId;
    private List<ProductDTO> products;

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Customer getCustomer() {
        return customerId;
    }

    public void setCustomer(Customer customerId) {
        this.customerId = customerId;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
