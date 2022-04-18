package jpabook.jpabookstore.controller;

import jpabook.jpabookstore.domain.Item;
import jpabook.jpabookstore.domain.Member;
import jpabook.jpabookstore.domain.OrderSearch;
import jpabook.jpabookstore.domain.Orders;
import jpabook.jpabookstore.service.ItemService;
import jpabook.jpabookstore.service.MemberService;
import jpabook.jpabookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;


    @GetMapping("/order")
    public String createForm(Model model){
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();
        model.addAttribute("members",members);
        model.addAttribute("items",items);
        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count){

        orderService.order(memberId,itemId,count);

        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model){

        List<Orders> orderList = orderService.findOrders(orderSearch);
        model.addAttribute("orders",orderList);

        return "order/orderList";

    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId){
        orderService.cancelOrder(orderId);

        return "redirect:/orders";
    }
}
