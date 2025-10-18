package icu.wenxin.grocery.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import icu.wenxin.grocery.common.resp.R;
import icu.wenxin.grocery.domain.dto.ProductDTO;
import icu.wenxin.grocery.domain.po.Product;
import icu.wenxin.grocery.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public R<Page<Product>> getPage(ProductDTO dto) {
        return productService.getList(dto);
    }

    @GetMapping("/get")
    public R<Product> getProductByIdOrCode(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "code", required = false) String code) {
        return productService.getProductByIdOrCode(id, code);
    }

    @PostMapping
    public R<String> addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @PutMapping
    public R<String> updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public R<String> deleteProduct(@PathVariable Integer id) {
        return productService.removeById(id) ? R.ok() : R.error();
    }

}
