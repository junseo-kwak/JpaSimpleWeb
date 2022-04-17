package jpabook.jpabookstore.service;

import jpabook.jpabookstore.domain.*;
import jpabook.jpabookstore.repository.ItemRepository;
import jpabook.jpabookstore.repository.MemberRepository;
import jpabook.jpabookstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;


    /*
      주문 생성
     */

    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        // 엔티티 조회
        Member member = memberRepository.find(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송지
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item,item.getPrice(),count);

        // 주문 생성
        Orders order = Orders.createOrder(member,delivery,orderItem);

        orderRepository.save(order);

        return order.getId();
    }


    /*
     주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId){
        Orders orders = orderRepository.findOne(orderId);

        orders.cancel();
    }
    
    /*
     검색
     */

    public List<Orders> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByCriteria(orderSearch);
    }
}
