package icu.wenxin.grocery.task;

import icu.wenxin.grocery.business.service.ProductImageService;
import icu.wenxin.grocery.minio.utils.MinioUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroceryTask {
    private final MinioUtils minioUtils;
    private final ProductImageService productImageService;


//    @Scheduled(cron = "0 0 0 * * ?")
//    public void clearTempFilesTask() {
//        try {
//            log.info("开始清理临时图片");
//            List<ProductImage> list = productImageService
//                    .lambdaQuery().eq(ProductImage::getProductId, null)
//                    .list();
//            if (list.isEmpty()) {
//                log.info("暂无临时图片");
//                return;
//            }
//            List<String> imageList = list.stream().map(ProductImage::getUrl).toList();
//            log.info("待清理图片：{}", imageList);
//            List<DeleteError> deleteErrors = minioUtils.deleteFiles(imageList);
//            if (deleteErrors.isEmpty()) {
//                log.info("清理临时图片成功");
//            } else {
//                log.error("清理临时图片失败：{}", deleteErrors);
//            }
//        } catch (Exception e) {
//            log.error("清理临时图片失败：{}", e.getMessage(), e);
//        }
//    }
}
