package org.star5025.backend.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "stdl.jwt")
@Data
public class JwtProperties {

    /**
     * 生成jwt令牌相关配置，根据application.yml
     */
    private String secretKey;
    private long ttl;
    private String tokenName;

}
