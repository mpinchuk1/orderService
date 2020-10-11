package appMain.controllers;

import appMain.entities.Seller;
import appMain.entities.dto.*;
import appMain.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody CreateOrderDTO createOrder){
        Seller seller = createOrder.getSeller();
        Customer customer = createOrder.getCustomer();
        System.out.println(customer);
        List<ProductDTO> products = createOrder.getProducts();

        if(orderService.addOrder(seller, products, customer))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public @ResponseBody OrdersDTO getAllOrders(){
        OrdersDTO ordersDTO = new OrdersDTO();
        ordersDTO.setOrders(orderService.getAllOrders());
        return ordersDTO;
    }

    @GetMapping("/orderProducts")
    public @ResponseBody
    OrderProductsDTO getAllOrderProducts(){
        OrderProductsDTO ordersDTO = new OrderProductsDTO();
        ordersDTO.setOrderProducts(orderService.getAllOrderProducts());
        return ordersDTO;
    }
}
