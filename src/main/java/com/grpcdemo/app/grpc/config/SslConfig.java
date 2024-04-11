package com.grpcdemo.app.grpc.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SslConfig {
    @Value("${server.ssl.certificate-private-key}")
    private String certificatePrivateKey;

    @Value("${server.ssl.certificate}")
    private String certificate;

    @Value("${server.trust-certificate}")
    private String trustCertificate;

    @Value("${app.worker.disable.https:true}")
    private boolean isSslDisabledOnWorker;

    public boolean isSslEnabledForWorker() {
        return !isSslDisabledOnWorker;
    }
}
