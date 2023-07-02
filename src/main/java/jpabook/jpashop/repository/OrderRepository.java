package jpabook.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import java.util.List;

import static jpabook.jpashop.domain.QMember.member;
import static jpabook.jpashop.domain.QOrder.order;


@Repository
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public OrderRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {

        QOrder order = QOrder.order;
        QMember member = QMember.member;

        return query
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()),
                        nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression nameLike(String memberName) {
        if (StringUtils.isEmptyOrWhitespace(memberName)) {
            return null;
        }
        return member.name.contains(memberName);
    }

    private BooleanExpression statusEq(OrderStatus orderStatus) {
        if (orderStatus == null) {
            return null;
        }
        return order.status.eq(orderStatus);

    }

    public List<Order> findAllWithMemberDelivery(OrderSearch orderSearch) {
        return em.createQuery(
                "select o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d", Order.class
        ).getResultList();
    }

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return
        em.createQuery("select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o " +
                        "join o.member m " +
                        "join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();

    }


    /***
     *
     * 일대다는 페이징 처리가 불가능! (가능은 한 데 쓰지 말기)
     * 왜냐하면 메모리에서 페이징 해버리기 때문에
     *
     * 또한 컬렉션 페치 조인은 한 개에만 쓰자!
     */
    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o " +
                        "join  fetch  o.member m " +
                        "join  fetch  o.delivery d " +
                        "join  fetch  o.orderItems oi " +
                        "join  fetch  oi.item", Order.class
        ).getResultList();
    }


    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();

    }
}
