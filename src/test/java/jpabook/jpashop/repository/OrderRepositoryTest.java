package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    EntityManager em;

    @Test
    void orderRepoTest() {
        Member member = new Member();
        member.setName("승진");
        member.setAddress(new Address("서울", "강가", "123-123"));
        Item item = new Book();
        item.setName("JPA");
        item.setPrice(10000);
        item.setStockQuantity(10);
        em.persist(item);
        em.persist(member);

        Long order = orderService.order(member.getId(), item.getId(), 2);

        Order findOrder = orderRepository.findOne(order);
        List<Order> orders = orderRepository.findAll(new OrderSearch("승", OrderStatus.ORDER));
        if (orders.isEmpty()) {
            System.out.println("Empty");
            return;
        }
        for (Order order1 : orders) {
            System.out.println("order1 = " + order1.getMember().getName());
        }
    }

}