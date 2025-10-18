package icu.wenxin.grocery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.wenxin.grocery.common.resp.R;
import icu.wenxin.grocery.domain.dto.ProductDTO;
import icu.wenxin.grocery.domain.po.Product;

public interface ProductService extends IService<Product> {
    R<Page<Product>> getList(ProductDTO dto);

    R<String> addProduct(Product product);

    R<String> updateProduct(Product product);

    R<Product> getProductByIdOrCode(Integer id, String code);
}
