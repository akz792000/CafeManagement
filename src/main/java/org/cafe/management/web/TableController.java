package org.cafe.management.web;

import lombok.RequiredArgsConstructor;
import org.cafe.management.domain.TableEntity;
import org.cafe.management.domain.UserEntity;
import org.cafe.management.enums.RoleType;
import org.cafe.management.repository.TableRepository;
import org.cafe.management.repository.UserRepository;
import org.cafe.management.web.dto.TableDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Controller
@RequestMapping("/table")
public class TableController {

    private final TableRepository tableRepository;

    private final UserRepository userRepository;

    @GetMapping(value = "/entry")
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("table-edit");
        modelAndView.addObject("dto", new TableDto());
        return modelAndView;
    }

    @GetMapping(value = "/list/page/{page}")
    public ModelAndView getPageByPage(@PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView("table-list");
        PageRequest pageable = PageRequest.of(page - 1, 5);
        Page<TableEntity> articlePage = tableRepository.findAll(pageable);
        int totalPages = articlePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
        modelAndView.addObject("items", articlePage.getContent());
        return modelAndView;
    }

    @PostMapping(value = "/persist")
    public String persist(@ModelAttribute("dto") @Valid TableDto dto, BindingResult result) {
        TableEntity entity = tableRepository.findByName(dto.getName());
        if (entity != null) {
            result.rejectValue("name", null, "Name is exist.");
        }
        if (result.hasErrors()) {
            return "table-edit";
        }
        TableEntity newEntity = new TableEntity();
        newEntity.setName(dto.getName());
        tableRepository.save(newEntity);
        return "redirect:/table/entry?success";
    }

    @GetMapping(value = "/user/{name}")
    public ModelAndView getUser(@PathVariable("name") String name) {
        ModelAndView modelAndView = new ModelAndView("table-user");
        TableEntity entity = tableRepository.findByName(name);
        TableDto dto = new TableDto();
        dto.setName(entity.getName());
        dto.setUsername(entity.getUser() == null ? null : entity.getUser().getUsername());
        modelAndView.addObject("dto", dto);
        List<String> users = new ArrayList<>();
        for (UserEntity user : userRepository.findByRoleType(RoleType.ROLE_WAITER)) {
            users.add(user.getUsername());
        }
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @PostMapping(value = "/user/persist")
    public String userPersist(@ModelAttribute("dto") @Valid TableDto dto, BindingResult result) {
        TableEntity tableEntity = tableRepository.findByName(dto.getName());
        if (tableEntity == null) {
            result.rejectValue("name", null, "Name is not exist.");
        }
        UserEntity userEntity = userRepository.findByUsername(dto.getUsername());
        if (userEntity == null) {
            result.rejectValue("username", null, "Username is not exist.");
        }
        tableEntity.setUser(userEntity);
        if (result.hasErrors()) {
            return "table-user";
        }
        tableRepository.save(tableEntity);
        return "redirect:/table/user/" + tableEntity.getName() + "?success";
    }

}
