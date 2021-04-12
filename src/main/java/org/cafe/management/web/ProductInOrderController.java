package org.cafe.management.web;

import lombok.RequiredArgsConstructor;
import org.cafe.management.domain.OrderEntity;
import org.cafe.management.domain.ProductEntity;
import org.cafe.management.domain.ProductInOrderEntity;
import org.cafe.management.enums.ProductInOrderStatusType;
import org.cafe.management.enums.RoleType;
import org.cafe.management.repository.OrderRepository;
import org.cafe.management.repository.ProductInOrderRepository;
import org.cafe.management.repository.ProductRepository;
import org.cafe.management.web.dto.ProductInOrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Ali Karimizandi
 * @since 2021
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/product-in-order")
public class ProductInOrderController {

    private final ProductInOrderRepository productInOrderRepository;

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    @RolesAllowed(RoleType.Name.ROLE_WAITER)
    @GetMapping(value = "/entry/order/{orderId}")
    public ModelAndView entry(@PathVariable("orderId") Long orderId) {
        ModelAndView modelAndView = new ModelAndView("product-in-order-entry");
        ProductInOrderEntity productInOrderEntity = new ProductInOrderEntity();
        OrderEntity orderEntity = orderRepository.findById(orderId).get();
        modelAndView.addObject("tableName", orderEntity.getTable().getName());
        modelAndView.addObject("products", productRepository.findAll().stream().map(ProductEntity::getName).collect(Collectors.toList()));
        productInOrderEntity.setOrder(orderEntity);
        modelAndView.addObject("dto", productInOrderEntity);
        return modelAndView;
    }

    @RolesAllowed(RoleType.Name.ROLE_WAITER)
    @GetMapping(value = "/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("product-in-order-edit");
        ProductInOrderEntity productInOrderEntity = productInOrderRepository.findById(id).get();
        OrderEntity orderEntity = orderRepository.findById(productInOrderEntity.getOrder().getId()).get();
        modelAndView.addObject("tableName", orderEntity.getTable().getName());
        modelAndView.addObject("dto", productInOrderEntity);
        return modelAndView;
    }

    @RolesAllowed(RoleType.Name.ROLE_WAITER)
    @GetMapping(value = "/order/{orderId}/list/page/{page}")
    public ModelAndView get(@PathVariable("orderId") Long orderId, @PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView("product-in-order-list");
        PageRequest pageable = PageRequest.of(page - 1, 5);
        Page<ProductInOrderEntity> articlePage = productInOrderRepository.findAllByOrderId(orderId, pageable);
        int totalPages = articlePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
        OrderEntity orderEntity = orderRepository.findById(orderId).get();
        modelAndView.addObject("tableName", orderEntity.getTable().getName());
        modelAndView.addObject("orderId", orderEntity.getId());
        modelAndView.addObject("orderName", orderEntity.getName());
        modelAndView.addObject("items", articlePage.getContent());
        return modelAndView;
    }

    @RolesAllowed(RoleType.Name.ROLE_WAITER)
    @PostMapping(value = "/persist")
    public String persist(@ModelAttribute("dto") @Valid ProductInOrderDto dto, BindingResult result) {
        Optional<OrderEntity> entity = orderRepository.findById(dto.getOrder().getId());
        if (!entity.isPresent()) {
            result.rejectValue("order.name", null, "order not exist");
        }
        if (result.hasErrors()) {
            return "product-in-order-entry";
        }
        ProductInOrderEntity newEntity = new ProductInOrderEntity();
        newEntity.setOrder(orderRepository.findById(dto.getOrder().getId()).get());
        newEntity.setProduct(productRepository.findByName(dto.getProduct().getName()));
        newEntity.setAmount(dto.getAmount());
        newEntity.setStatus(ProductInOrderStatusType.ACTIVE);
        productInOrderRepository.save(newEntity);
        return "redirect:/product-in-order/entry/order/" + dto.getOrder().getId() + "/?success";
    }

    @RolesAllowed(RoleType.Name.ROLE_WAITER)
    @PostMapping(value = "/merge")
    public String merge(@ModelAttribute("dto") @Valid ProductInOrderDto dto, BindingResult result) {
        Optional<ProductInOrderEntity> entity = productInOrderRepository.findById(dto.getId());
        if (!entity.isPresent()) {
            result.rejectValue("status", null, "product in order not exist");
        }
        if (result.hasErrors()) {
            return "product-in-order-edit";
        }
        entity.get().setAmount(dto.getAmount());
        entity.get().setStatus(dto.getStatus());
        productInOrderRepository.save(entity.get());
        return "redirect:/product-in-order/order/" + dto.getOrder().getId() + "/list/page/1";
    }

}
