package org.cafe.management.web;

import lombok.RequiredArgsConstructor;
import org.cafe.management.domain.OrderEntity;
import org.cafe.management.domain.TableEntity;
import org.cafe.management.domain.UserEntity;
import org.cafe.management.enums.OrderStatusType;
import org.cafe.management.enums.RoleType;
import org.cafe.management.repository.OrderRepository;
import org.cafe.management.repository.TableRepository;
import org.cafe.management.util.SecurityUtils;
import org.cafe.management.web.dto.OrderDto;
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
@RequestMapping("/order")
public class OrderController {

    private final TableRepository tableRepository;

    private final OrderRepository orderRepository;

    @RolesAllowed(RoleType.Name.ROLE_WAITER)
    @GetMapping(value = "/entry/{tableName}")
    public ModelAndView entry(@PathVariable("tableName") String tableName) {
        ModelAndView modelAndView = new ModelAndView("order-entry");
        OrderEntity orderEntity = new OrderEntity();
        TableEntity tableEntity = tableRepository.findByName(tableName);
        orderEntity.setTable(tableEntity);
        modelAndView.addObject("dto", orderEntity);
        return modelAndView;
    }

    @RolesAllowed(RoleType.Name.ROLE_WAITER)
    @GetMapping(value = "/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("order-edit");
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);
        modelAndView.addObject("dto", orderEntity.get());
        return modelAndView;
    }

    @RolesAllowed(RoleType.Name.ROLE_WAITER)
    @GetMapping(value = "/table/{tableName}/list/page/{page}")
    public ModelAndView get(@PathVariable("tableName") String tableName, @PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView("order-list");
        PageRequest pageable = PageRequest.of(page - 1, 5);
        Page<OrderEntity> articlePage = orderRepository.findByTableNameAndTableUserId(tableName, SecurityUtils.getUserDetails().getId(), pageable);
        int totalPages = articlePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
        modelAndView.addObject("tableName", tableName);
        modelAndView.addObject("items", articlePage.getContent());
        return modelAndView;
    }

    @RolesAllowed(RoleType.Name.ROLE_WAITER)
    @PostMapping(value = "/persist")
    public String persist(@ModelAttribute("dto") @Valid OrderDto dto, BindingResult result) {
        if (orderRepository.existsByTableNameAndStatus(dto.getTable().getName(), OrderStatusType.OPEN)) {
            result.rejectValue("name", null, "There is an order with status type open existed for this table.");
        }
        if (result.hasErrors()) {
            return "order-entry";
        }
        OrderEntity newEntity = new OrderEntity();
        newEntity.setName(dto.getName());
        newEntity.setStatus(OrderStatusType.OPEN);
        TableEntity tableEntity = tableRepository.findByName(dto.getTable().getName());
        newEntity.setTable(tableEntity);
        orderRepository.save(newEntity);
        return "redirect:/order/entry/" + dto.getTable().getName() + "/?success";
    }

    @RolesAllowed(RoleType.Name.ROLE_WAITER)
    @PostMapping(value = "/merge")
    public String merge(@ModelAttribute("dto") @Valid OrderDto dto, BindingResult result) {
        Optional<OrderEntity> entity = orderRepository.findById(dto.getId());
        if (!entity.isPresent()) {
            result.rejectValue("status", null, "order not exist");
        }
        if (result.hasErrors()) {
            return "order-edit";
        }
        entity.get().setStatus(dto.getStatus());
        orderRepository.save(entity.get());
        return "redirect:/order/table/" + dto.getTable().getName() + "/list/page/1";
    }

}
