package icu.wenxin.grocery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.wenxin.grocery.domain.po.ProductImage;
import icu.wenxin.grocery.mapper.ProductImageMapper;
import icu.wenxin.grocery.properties.MinioProperties;
import icu.wenxin.grocery.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl extends ServiceImpl<ProductImageMapper, ProductImage> implements ProductImageService {

    private final MinioProperties properties;

    @Override
    public List<ProductImage> getListByProductId(Integer id) {
        List<ProductImage> list = lambdaQuery().eq(ProductImage::getProductId, id).list();
        list.forEach(image -> image.setUrl(properties.getBaseUrl(image.getName())));
        return list;
    }
}
