package icu.wenxin.grocery.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import icu.wenxin.grocery.business.domain.po.ProductImage;

import java.util.List;

public interface ProductImageService extends IService<ProductImage> {
    List<ProductImage> getListByProductId(Integer id);
}
