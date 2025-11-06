package icu.wenxin.grocery.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.wenxin.grocery.business.domain.po.ProductImage;
import icu.wenxin.grocery.business.mapper.ProductImageMapper;
import icu.wenxin.grocery.minio.properties.MinioProperties;
import icu.wenxin.grocery.business.service.ProductImageService;
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
