package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 상품주문() {
        Member member = new Member();
        member.setName("승진");
        member.setAddress(new Address("서울", "강가", "123-123"));
        Item item = new Book();
        item.setName("JPA");
        item.setPrice(10000);
        item.setStockQuantity(10);

        em.persist(member);
        em.persist(item);

        Long order = orderService.order(member.getId(), item.getId(), 2);

        Order findOrder = orderRepository.findOne(order);
        assertThat(findOrder.getId()).isEqualTo(order);
        System.out.println("item = " + item.getStockQuantity());


    }

    @Test
    void 상품주문_재고초과() {
        Member member = new Member();
        member.setName("승진");
        member.setAddress(new Address("서울", "강가", "123-123"));
        Item item = new Book();
        item.setName("JPA");
        item.setPrice(10000);
        item.setStockQuantity(10);

        em.persist(member);
        em.persist(item);


        assertThatThrownBy(() ->
                orderService.order(member.getId(), item.getId(), 12))
                .isInstanceOf(NotEnoughStockException.class);
    }

    @Test
    void 주문취소() {
        Member member = new Member();
        member.setName("승진");
        member.setAddress(new Address("서울", "강가", "123-123"));
        Item item = new Book();
        item.setName("JPA");
        item.setPrice(10000);
        item.setStockQuantity(10);

        em.persist(member);
        em.persist(item);

        Long order = orderService.order(member.getId(), item.getId(), 2);

        orderService.cancelOrder(order);
        Order findOrder = orderRepository.findOne(order);

        assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(item.getStockQuantity()).isEqualTo(10);
    }


}