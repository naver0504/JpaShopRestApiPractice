package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static jpabook.jpashop.domain.Order.createOrder;
import static jpabook.jpashop.domain.OrderItem.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findById(memberId).get();
        Item item = itemRepository.findById(itemId).get();

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        OrderItem orderItem = createOrderItem(item, item.getPrice(), count);
        Order order = createOrder(member, delivery, orderItem);

        orderRepository.save(order);
        return order.getId();

    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }

    public List<Order> findOrders3_3(int offset, int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        orders.stream()
                .forEach(o -> {
                    o.getOrderItems().size();
                    log.info("order = {}", o);
                    o.getOrderItems().stream()
                            .forEach(oi -> {
                                oi.getItem().getName();
                            });
                });

        return orders;

    }


}
