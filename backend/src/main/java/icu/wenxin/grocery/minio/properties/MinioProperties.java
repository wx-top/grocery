package icu.wenxin.grocery.minio.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("minio")
public class MinioProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucket;

    public String getBaseUrl() {
        return endpoint + "/" + bucket + "/";
    }

    public String getBaseUrl(String fileName) {
        return getBaseUrl() + fileName;
    }
}
