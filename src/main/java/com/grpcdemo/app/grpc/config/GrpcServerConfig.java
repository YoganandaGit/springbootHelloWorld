package com.grpcdemo.app.grpc.config;

import com.grpcdemo.app.grpc.exception.InvalidConfigException;
import com.grpcdemo.app.grpc.service.JobInfoServiceGrpcImpl;
import io.grpc.Grpc;
import io.grpc.Server;

import io.grpc.ServerBuilder;
import io.grpc.TlsServerCredentials;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "demo.grpc.app")
public class GrpcServerConfig {
    private @Setter int port;
    private @Setter int maxInboundMessageSizeMB;
    private @Setter int maxConnectionAgeSecs;
    private @Setter int maxConnectionIdleSecs;

    @Bean
    public Server grpcServer(SslConfig sslConfig, JobInfoServiceGrpcImpl jobInfoServiceGrpc) throws IOException {
        log.info("Before starting grpc server on port: {}, isSslEnabledForWorker: {}, maxInboundMessageSizeMB: {}, maxConnectionAgeSecs: {}, maxConnectionIdleSecs: {}"
                , port, sslConfig.isSslEnabledForWorker(), maxInboundMessageSizeMB, maxConnectionAgeSecs, maxConnectionIdleSecs);

        ServerBuilder<?> serverBuilder = null;
        if (sslConfig.isSslEnabledForWorker()) {
            TlsServerCredentials.Builder credBuilder = TlsServerCredentials.newBuilder();
            if (StringUtils.isNotBlank(sslConfig.getTrustCertificate())) {
                credBuilder.trustManager(new File(sslConfig.getTrustCertificate()));
            }
            if (StringUtils.isBlank(sslConfig.getCertificate())) {
                throw new InvalidConfigException("Missing value for `server.ssl.certificate`");
            }

            if (StringUtils.isBlank(sslConfig.getCertificatePrivateKey())) {
                throw new InvalidConfigException("Missing value for `server.ssl.certificate-private-key`");
            }
            credBuilder.keyManager(new File(sslConfig.getCertificate()), new File(sslConfig.getCertificatePrivateKey()));
            serverBuilder = Grpc.newServerBuilderForPort(port, credBuilder.build()).addService(jobInfoServiceGrpc);
        } else {
            serverBuilder = ServerBuilder.forPort(port).addService(jobInfoServiceGrpc);
        }

        if (maxConnectionAgeSecs > 0) {
            serverBuilder.maxConnectionAge(maxConnectionAgeSecs, TimeUnit.SECONDS);
        }
        if (maxConnectionIdleSecs > 0) {
            serverBuilder.maxConnectionIdle(maxConnectionIdleSecs, TimeUnit.SECONDS);
        }
        if (maxInboundMessageSizeMB > 0) {
            serverBuilder.maxInboundMessageSize(maxInboundMessageSizeMB * 1024 * 1024);
        }

        Server server = serverBuilder.build();
        server.start();
        log.info("After starting grpc server on port: {}", port);
        return server;
    }
}
