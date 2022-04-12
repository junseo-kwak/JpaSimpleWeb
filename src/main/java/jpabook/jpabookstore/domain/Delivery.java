package jpabook.jpabookstore.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    private Long id;

    @Embedded
    private Address address;

    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Orders order;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;



}
