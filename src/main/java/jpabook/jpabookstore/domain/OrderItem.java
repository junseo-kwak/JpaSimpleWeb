package jpabook.jpabookstore.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


    private int orderPrice;
    private int count;

    public void cancel() {
        getItem().addStock(count);
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
         OrderItem orderItem = new OrderItem();

         orderItem.setItem(item);
         orderItem.setOrderPrice(orderPrice);
         orderItem.setCount(count);
         item.removeStock(count);

        return orderItem;
    }
}
