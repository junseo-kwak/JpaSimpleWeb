package jpabook.jpabookstore.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.criteria.Order;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderItem> orderItems = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderDate;


    // == 연관관계 편의 메서드 == //

    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }
    // == 생성 메서드 == //
    public static Orders createOrder(Member member, Delivery delivery, OrderItem... orderItems){
            Orders order = new Orders();
            order.setMember(member);
            order.setDelivery(delivery);
            for(OrderItem orderItem : orderItems){
                order.addOrderItem(orderItem);
            }
            order.setOrderStatus(OrderStatus.ORDER);
            order.setOrderDate(LocalDateTime.now());
            return order;
    }

    // == 비즈니스 로직 == //
    /*
      주문취소
     */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다");
        }

        this.setOrderStatus(OrderStatus.CANCEL);

        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    // == 조회 로직 == //

    /*
      전체 주문 가격 조회
     */
     public int getTotalPrice(){
         int totalPrice = 0;
         for(OrderItem orderItem : orderItems){
             totalPrice += orderItem.getTotalPrice();
         }
        return totalPrice;
     }

}
