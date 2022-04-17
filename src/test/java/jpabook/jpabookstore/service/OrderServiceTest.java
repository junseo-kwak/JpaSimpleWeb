package jpabook.jpabookstore.service;

import jpabook.jpabookstore.domain.*;
import jpabook.jpabookstore.exception.NotEnoughStockException;
import jpabook.jpabookstore.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest  {
        @Autowired OrderRepository orderRepository;
        @Autowired OrderService orderService;
        @Autowired EntityManager em;

        @Test
        public void 주문생성_테스트() throws Exception{
            // given
            Member member = createMember();
            Book book = createBook("JPA 스타트", 10, 10000);
            int orderCount = 2;

            // when
            Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
            Orders findOrder = orderRepository.findOne(orderId);

            // then
            assertEquals("주문상태는 ORDER여야 합니다", OrderStatus.ORDER,findOrder.getOrderStatus());
            assertEquals("주문한 상품 종류 수가 동일해야 합니다.", 1,findOrder.getOrderItems().size());
            assertEquals("주문 가격은 상품 가격 * 수량입니다.", 10000 * orderCount, findOrder.getTotalPrice());
            assertEquals("주문한 만큼 주문 수량이 줄어야한다.", 8, book.getStockQuantity());


        }

        @Test(expected = NotEnoughStockException.class)
        public void 상품주문_재고수량초과(){

            // given
            Member member = createMember();
            Item item = createBook("시골 JPA", 10,10000);

            int orderCount = 10;
            // when
            orderService.order(member.getId(),item.getId(),orderCount);

            // then
            fail("재고 초과 예외가 터져야합니다.");


        }

        @Test
        public void 주문취소(){
            // given
            Member member = createMember();
            Item item = createBook("테스트 상품", 10,10000);
            Long orderId = orderService.order(member.getId(), item.getId(), 3);

            // when
            Orders findOrder = orderRepository.findOne(orderId);
            findOrder.cancel();

            // then
            assertEquals("주문취소 후 재고가 원복되어야합니다.", 10, item.getStockQuantity());
            assertEquals("주문 취소후 상태는 CANCEL", OrderStatus.CANCEL,findOrder.getOrderStatus());

        }


    private Book createBook(String name, int stockQuantity, int price) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("테스트 멤버");
        member.setAddress(new Address("서울","강가","12345"));
        em.persist(member);
        return member;
    }


}