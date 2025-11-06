package icu.wenxin.grocery;

import icu.wenxin.grocery.minio.utils.MinioUtils;
import io.minio.messages.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private MinioUtils minioUtils;

    @Test
    void contextLoads() {
        List<Item> items = minioUtils.listObjects();
        for (Item item : items) {
            System.out.println(item.objectName());
        }
    }

}
