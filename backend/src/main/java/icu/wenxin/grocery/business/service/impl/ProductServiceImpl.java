package icu.wenxin.grocery.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.wenxin.grocery.business.domain.dto.ProductCreateDTO;
import icu.wenxin.grocery.business.domain.dto.ProductQueryDTO;
import icu.wenxin.grocery.business.domain.dto.ProductUpdateDTO;
import icu.wenxin.grocery.business.domain.vo.ProductVO;
import icu.wenxin.grocery.common.resp.R;
import icu.wenxin.grocery.business.domain.po.Product;
import icu.wenxin.grocery.business.domain.po.ProductImage;
import icu.wenxin.grocery.business.mapper.ProductMapper;
import icu.wenxin.grocery.business.service.ProductImageService;
import icu.wenxin.grocery.business.service.ProductService;
import io.github.linpeilie.Converter;
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

    private final Converter converter;


    @Override
    public R<IPage<ProductVO>> getProductList(ProductQueryDTO productQueryDTO) {
        IPage<Product> page = lambdaQuery()
                .like(StringUtils.isNotBlank(productQueryDTO.getName()), Product::getName, productQueryDTO.getName())
                .eq(StringUtils.isNotBlank(productQueryDTO.getCode()), Product::getCode, productQueryDTO.getCode())
                .eq(productQueryDTO.getCategoryId() != null, Product::getCategoryId, productQueryDTO.getCategoryId())
                .orderByDesc(Product::getUpdateAt)
                .orderByDesc(Product::getCreateAt)
                .page(new Page<>(productQueryDTO.getCurrent(), productQueryDTO.getSize()));
        IPage<ProductVO> voPage = page.convert(item -> converter.convert(item, ProductVO.class));
        voPage.setRecords(voPage.getRecords()
                .stream()
                .peek(product -> product.setImageList(productImageService.getListByProductId(product.getId())))
                .toList());
        return R.ok(voPage);
    }

    @Override
    public R<ProductVO> getProductByIdOrCode(Integer id, String code) {
        Product product = null;
        if (id !=null) {
            product = getById(id);
        } else if (StringUtils.isNotBlank(code)) {
            product = lambdaQuery().eq(Product::getCode, code).one();
        }
        if (product != null) {
            ProductVO productVO = converter.convert(product, ProductVO.class);
            productVO.setImageList(productImageService.getListByProductId(product.getId()));
            return R.ok(productVO);
        }
        return R.error("商品不存在");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> deleteProduct(Integer id) {
        boolean exists = lambdaQuery().eq(Product::getId, id).exists();
        if (!exists) {
            return R.error("商品不存在");
        }
        return removeById(id) ? R.ok("删除成功", null) : R.error("删除失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> addProduct(ProductCreateDTO productCreateDTO) {
        // 查询商品编码是否已经存在
        if (StringUtils.isNotBlank(productCreateDTO.getCode()) && lambdaQuery().eq(Product::getCode, productCreateDTO.getCode()).exists()) {
            return R.error("商品编码已经存在");
        }
        Product product = converter.convert(productCreateDTO, Product.class);
        save(product);
        if (productCreateDTO.getImageList() != null) {
            productCreateDTO.getImageList().forEach(image -> image.setProductId(product.getId()));
            productImageService.saveBatch(productCreateDTO.getImageList());
        }
        return R.ok("添加成功", null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<String> updateProduct(ProductUpdateDTO productUpdateDTO) {
        Product dbUser = getById(productUpdateDTO.getId());
        if (dbUser == null) {
            return R.error("商品不存在");
        }
        // 查询商品编码是否已经存在
        if (StringUtils.isNotBlank(productUpdateDTO.getCode()) &&
                lambdaQuery().eq(Product::getCode, productUpdateDTO.getCode()).and(wrapper -> wrapper.ne(Product::getId, productUpdateDTO.getId())).exists()) {
            return R.error("商品编码已经存在");
        }
        Product product = converter.convert(productUpdateDTO, Product.class);
        updateById(product);
        LambdaUpdateWrapper<ProductImage> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ProductImage::getProductId, product.getId());
        productImageService.remove(wrapper);
        productUpdateDTO.getImageList().forEach(image -> image.setProductId(product.getId()));
        productImageService.saveBatch(productUpdateDTO.getImageList());
        return R.ok("更新成功", null);
    }
}
