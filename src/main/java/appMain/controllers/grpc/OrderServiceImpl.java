package appMain.controllers.grpc;

import appMain.entities.Order;
import appMain.entities.OrderProduct;
import appMain.entities.Seller;
import appMain.entities.dto.Customer;
import appMain.entities.dto.ProductDTO;
import appMain.services.OrderService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.appMain.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@GrpcService
public class OrderServiceImpl extends orderServiceGrpc.orderServiceImplBase {
    private final OrderService orderService;

    @Autowired
    public OrderServiceImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void getOrders(GetRequestOrder request, StreamObserver<GetResponseOrder> responseStreamObserver) {
        List<Order> orders = orderService.getAllOrders();

        List<ProtoOrder> protoOrders = new ArrayList<>();
        for (Order order : orders) {
            ProtoOrder protoOrder = ProtoOrder.newBuilder()
                    .setId(order.getId().toString())
                    .setCustomerId(order.getCustomerId().toString())
                    .setPrice(order.getPrice().longValue())
                    .build();
            protoOrders.add(protoOrder);
        }
        GetResponseOrder response = GetResponseOrder.newBuilder().addAllOrder(protoOrders).build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void getOrderedThing(GetRequestOrderedProduct request, StreamObserver<GetResponseOrderedProduct> responseStreamObserver) {
        List<OrderProduct> orderProducts = orderService.getAllOrderProducts();

        List<ProtoOrderedProduct> protoOrderedProducts = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts) {
            ProtoOrderedProduct protoOrderedThing = ProtoOrderedProduct.newBuilder()
                    .setProductId(orderProduct.getProductId().toString())
                    .setOrderId(orderProduct.getOrderId().toString())
                    .build();
            protoOrderedProducts.add(protoOrderedThing);
        }
        GetResponseOrderedProduct response = GetResponseOrderedProduct.newBuilder().addAllOrderedProducts(protoOrderedProducts).build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }

    @Override
    public void create(CreateOrderRequest request, StreamObserver<CreateOrderResponse> responseStreamObserver) {
        ProtoCustomer protoCustomer = request.getCustomer();
        ProtoSeller protoSeller = request.getSeller();
        Customer customer = new Customer(UUID.fromString(protoCustomer.getCustomerId()),
                protoCustomer.getFirstName(), protoCustomer.getLastName(), protoCustomer.getAge());
        Seller seller = new Seller(protoSeller.getFirstName(), protoSeller.getLastName());

        List<ProtoProduct> protoProducts = request.getProductsList();

        List<ProductDTO> productDTOList = new ArrayList<>();
        for (ProtoProduct protoProduct : protoProducts) {
            ProductDTO productDTO = new ProductDTO(
                    protoProduct.getName(), protoProduct.getPrice(),
                    protoProduct.getIsForAdult(), protoProduct.getQuantity());
            productDTOList.add(productDTO);
        }
        orderService.addOrder(seller, productDTOList, customer);

        CreateOrderResponse response = CreateOrderResponse.newBuilder().build();
        responseStreamObserver.onNext(response);
        responseStreamObserver.onCompleted();
    }
}