package icu.wenxin.grocery.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.wenxin.grocery.common.resp.R;
import icu.wenxin.grocery.domain.dto.ProductDTO;
import icu.wenxin.grocery.domain.po.Product;
import icu.wenxin.grocery.domain.po.ProductImage;
import icu.wenxin.grocery.mapper.ProductMapper;
import icu.wenxin.grocery.service.ProductImageService;
import icu.wenxin.grocery.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private final ProductImageService productImageService;


    @Override
    public R<Page<Product>> getList(ProductDTO dto) {
        log.info("查询商品参数：{}", dto);
        Page<Product> page = lambdaQuery()
                .eq(StringUtils.isNotBlank(dto.getName()), Product::getName, dto.getName())
                .eq(StringUtils.isNotBlank(dto.getCode()), Product::getCode, dto.getCode())
                .eq(dto.getCategoryId() != null, Product::getCategoryId, dto.getCategoryId())
                .orderByDesc(Product::getCreateAt)
                .orderByDesc(Product::getUpdateAt)
                .page(new Page<>(dto.getCurrent(), dto.getSize()));
        page.setRecords(page.getRecords().stream().peek(product -> product.setImageList(productImageService.getListByProductId(product.getId()))).toList());
        return R.ok(page);
    }

//    @Override
//    public R<Product> getProductById(Integer id) {
//        Product product = getById(id);
//        if (product != null) {
//            product.setImageList(productImageService.getListByProductId(product.getId()));
//            return R.ok(product);
//        }
//        return R.error(null,"商品不存在");
//    }

    @Override
    public R<Product> getProductByIdOrCode(Integer id, String code) {
        Product product = null;
        if (id !=null) {
            product = getById(id);
        } else if (StringUtils.isNotBlank(code)) {
            product = lambdaQuery().eq(Product::getCode, code).one();
        }
        if (product != null) {
            product.setImageList(productImageService.getListByProductId(product.getId()));
            return R.ok(product);
        }
        return R.error("商品不存在");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> addProduct(Product product) {
        // 查询商品编码是否已经存在
        if (lambdaQuery().eq(Product::getCode, product.getCode()).exists()) {
            return R.error("商品编码已经存在");
        }
        save(product);
        product.getImageList().forEach(image -> image.setProductId(product.getId()));
        productImageService.saveBatch(product.getImageList());
        return R.ok("添加成功", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> updateProduct(Product product) {
        // 查询商品编码是否已经存在
        if (lambdaQuery().eq(Product::getCode, product.getCode()).ne(Product::getId, product.getId()).exists()) {
            return R.error("商品编码已经存在");
        }
        updateById(product);
        LambdaUpdateWrapper<ProductImage> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ProductImage::getProductId, product.getId());
        productImageService.remove(wrapper);
        product.getImageList().forEach(image -> image.setProductId(product.getId()));
        productImageService.saveBatch(product.getImageList());
        return R.ok("更新成功", null);
    }


}
