package appMain.services;

import appMain.entities.Order;
import appMain.entities.OrderProduct;
import appMain.entities.Seller;
import appMain.entities.dto.Customer;
import appMain.entities.dto.ProductDTO;
import appMain.repo.OrderProductRepository;
import appMain.repo.OrderRepository;
import appMain.repo.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final String COURIER_URL = "http://courierservice:8082";
    private final RestTemplate restTemplate = new RestTemplate();
    private final HttpHeaders headers = new HttpHeaders();
    private final HttpEntity<Object> headersEntity = new HttpEntity<>(headers);
    private final OrderRepository orderRepository;
    private final SellerRepository sellerRepository;
    private final OrderProductRepository orderProductRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, SellerRepository sellerRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.sellerRepository = sellerRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @Transactional
    public boolean addOrder(Seller seller, List<ProductDTO> requiredProducts, Customer customer) {
        List<ProductDTO> productsToOrder = new ArrayList<>();
        Seller sellerToOrder = sellerRepository
                .findSellerByFirstNameAndLastName(seller.getFirstName(), seller.getLastName());

        requiredProducts = requiredProducts.stream()
                .filter(p -> !p.isForAdult() || customer.getAge() >= 18)
                .collect(Collectors.toList());

        for (ProductDTO p : requiredProducts) {
            ProductDTO product = getProductFromServiceByName(p.getName());
            int prodQuantity = product.getQuantity();

            if (prodQuantity > 0) {
                productsToOrder.add(product);
                int newQuantity = prodQuantity - 1;
                p.setQuantity(newQuantity);
                saveNewQuantityForProduct(p);
            } else {
                System.out.println("There is no " + p.getName() + ". It has been already sold.");
            }
        }
        return createOrder(productsToOrder, sellerToOrder, customer);
    }

    private void saveNewQuantityForProduct(ProductDTO productWithNewQuantity) {
        HttpEntity<ProductDTO> product = new HttpEntity<>(productWithNewQuantity);
        ResponseEntity<Void> response = restTemplate
                .exchange(COURIER_URL + "/products/newQuantity",
                        HttpMethod.PUT, product, Void.class);
    }

    private boolean createOrder(List<ProductDTO> productsToOrder, Seller seller, Customer customer) {
        boolean flag = false;
        UUID id = UUID.randomUUID();
        double price = productsToOrder.stream().mapToDouble(ProductDTO::getPrice).sum();
        Order order = new Order(id, seller, customer.getCustomerId(), price);
        for (ProductDTO p : productsToOrder) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrderId(order.getId());
            orderProduct.setProductId(p.getProductId());
            System.out.println("New order has created: " + order);
            orderRepository.save(order);
            orderProductRepository.save(orderProduct);
            flag = true;
        }
        return flag;
    }

    private ProductDTO getProductFromServiceByName(String productName) {
        ResponseEntity<ProductDTO> response = restTemplate
                .exchange(COURIER_URL + "/products/name=" + productName,
                        HttpMethod.GET, headersEntity, ProductDTO.class);
        return response.getBody();
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<OrderProduct> getAllOrderProducts(){
        return orderProductRepository.findAll();
    }

}
