package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Data
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;

    public OrderSearch() {

    }
}
