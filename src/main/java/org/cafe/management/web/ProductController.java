package org.cafe.management.web;

import lombok.RequiredArgsConstructor;
import org.cafe.management.domain.ProductEntity;
import org.cafe.management.repository.ProductRepository;
import org.cafe.management.web.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/product")
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping(value = "/entry")
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("product-edit");
        modelAndView.addObject("dto", new ProductDto());
        return modelAndView;
    }

    @GetMapping(value = "/list/page/{page}")
    public ModelAndView getPageByPage(@PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView("product-list");
        PageRequest pageable = PageRequest.of(page - 1, 5);
        Page<ProductEntity> articlePage = productRepository.findAll(pageable);
        int totalPages = articlePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
        modelAndView.addObject("items", articlePage.getContent());
        return modelAndView;
    }

    @PostMapping(value = "/persist")
    public String persist(@ModelAttribute("dto") @Valid ProductDto dto, BindingResult result) {
        ProductEntity entity = productRepository.findByName(dto.getName());
        if (entity != null) {
            result.rejectValue("name", null, "Name is exist.");
        }
        if (result.hasErrors()) {
            return "product-edit";
        }
        ProductEntity newEntity = new ProductEntity();
        newEntity.setName(dto.getName());
        productRepository.save(newEntity);
        return "redirect:/product/entry?success";
    }

}
