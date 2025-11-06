package icu.wenxin.grocery.business.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import icu.wenxin.grocery.business.domain.dto.ProductCreateDTO;
import icu.wenxin.grocery.business.domain.dto.ProductQueryDTO;
import icu.wenxin.grocery.business.domain.dto.ProductUpdateDTO;
import icu.wenxin.grocery.business.domain.vo.ProductVO;
import icu.wenxin.grocery.common.resp.R;
import icu.wenxin.grocery.business.domain.po.Product;

public interface ProductService extends IService<Product> {
    R<IPage<ProductVO>> getProductList(ProductQueryDTO productQueryDTO);

    R<String> addProduct(ProductCreateDTO product);

    R<String> updateProduct(ProductUpdateDTO product);

    R<ProductVO> getProductByIdOrCode(Integer id, String code);

    R<String> deleteProduct(Integer id);
}
