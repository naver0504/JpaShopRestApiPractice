package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public List<OrderDtoV2> orderV3_2(int offset, int limit) {
        return orderRepository.findAllWithMemberDelivery(offset, limit)
                .stream()
                .map(o -> new OrderDtoV2(o))
                .collect(Collectors.toList());
    }



    @Getter
    public static class OrderDtoV2 {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDtoV2> orderItems;

        public OrderDtoV2(Order o) {
            orderId = o.getId();
            name = o.getMember().getName();
            orderDate = o.getOrderDate();
            address = o.getDelivery().getAddress();
            ;
            orderStatus = o.getStatus();
            orderItems = o.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDtoV2(orderItem))
                    .collect(Collectors.toList());

        }
    }

    @Getter
    public static class OrderItemDtoV2 {

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDtoV2(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
