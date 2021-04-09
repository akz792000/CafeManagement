package org.cafe.management.web;

import lombok.RequiredArgsConstructor;
import org.cafe.management.domain.UserEntity;
import org.cafe.management.repository.UserRepository;
import org.cafe.management.web.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping(value = "/entry")
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("user-edit");
        modelAndView.addObject("dto", new UserDto());
        return modelAndView;
    }

    @GetMapping(value = "/list/page/{page}")
    public ModelAndView getPageByPage(@PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView("user-list");
        PageRequest pageable = PageRequest.of(page - 1, 5);
        Page<UserEntity> articlePage = userRepository.findAll(pageable);
        int totalPages = articlePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
        modelAndView.addObject("items", articlePage.getContent());
        return modelAndView;
    }

    @PostMapping(value = "/persist")
    public String persist(@ModelAttribute("dto") @Valid UserDto dto, BindingResult result) {
        UserEntity entity = userRepository.findByUsername(dto.getUsername());
        if (entity != null) {
            result.rejectValue("username", null, "Username is exist.");
        }
        if (result.hasErrors()) {
            return "user-edit";
        }
        UserEntity newEntity = new UserEntity();
        newEntity.setUsername(dto.getUsername());
        newEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        newEntity.setRoleType(dto.getRoleType());
        userRepository.save(newEntity);
        return "redirect:/user/entry?success";
    }

}
