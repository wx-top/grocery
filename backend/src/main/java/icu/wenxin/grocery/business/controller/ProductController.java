package icu.wenxin.grocery.business.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import icu.wenxin.grocery.business.domain.dto.ProductCreateDTO;
import icu.wenxin.grocery.business.domain.dto.ProductQueryDTO;
import icu.wenxin.grocery.business.domain.dto.ProductUpdateDTO;
import icu.wenxin.grocery.business.domain.vo.ProductVO;
import icu.wenxin.grocery.common.resp.R;
import icu.wenxin.grocery.business.service.ProductService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public R<IPage<ProductVO>> getProductList(ProductQueryDTO productQueryDTO) {
        return productService.getProductList(productQueryDTO);
    }

    @GetMapping("/get")
    public R<ProductVO> getProductByIdOrCode(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "code", required = false) String code) {
        return productService.getProductByIdOrCode(id, code);
    }

    @PostMapping
    public R<String> addProduct(@RequestBody ProductCreateDTO productCreateDTO) {
        return productService.addProduct(productCreateDTO);
    }

    @PutMapping
    public R<String> updateProduct(@RequestBody ProductUpdateDTO productUpdateDTO) {
        return productService.updateProduct(productUpdateDTO);
    }

    @DeleteMapping("/{id}")
    public R<String> deleteProduct(@PathVariable @NotNull(message = "ID不能为空") Integer id) {
        String a = "a";
        a = a(a);
        return productService.deleteProduct(id);
    }


    public String a(String a) {
        return a;
    }

}
