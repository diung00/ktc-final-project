package com.example.ChoiGangDeliveryApp.api.ncpmaps.config;

import com.example.ChoiGangDeliveryApp.api.ncpmaps.client.NcpGeolocationInterface;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.client.NcpHttpInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Configuration
public class NcpClientConfig {
    @Value("${ncp.api.client-id}")
    private String clientId;
    @Value("${ncp.api.client-secret}")
    private String clientSecret;
    @Bean
    public RestClient ncpMapClient() {
        return RestClient.builder()
                .baseUrl("https://naveropenapi.apigw.ntruss.com")
                .defaultHeader("X-NCP-APIGW-API-KEY-ID", clientId)
                .defaultHeader("X-NCP-APIGW-API-KEY", clientSecret)
                .build();
    }

    @Bean
    public RestClient ncpGeoLocationClient() {
        return RestClient.builder()
                .baseUrl("https://geolocation.apigw.ntruss.com/geolocation/v2/geoLocation")
                .requestInitializer(request -> {
                    HttpHeaders headers = request.getHeaders();

                    // 1. Timestamps cho yêu cầu
                    long timestamp = System.currentTimeMillis();
                    headers.add("x-ncp-apigw-timestamp", Long.toString(timestamp));

                    // 2. Thêm AccessKey (sử dụng clientId hoặc clientSecret, tùy vào API yêu cầu)
                    headers.add("x-ncp-iam-access-key", clientId); // hoặc có thể sử dụng một giá trị khác nếu cần

                    // 3. Tạo Signature
                    String signature = this.makeSignature(request.getMethod(),
                            request.getURI().getPath() + "?" + request.getURI().getQuery(), timestamp);
                    headers.add("x-ncp-apigw-signature-v2", signature);
                })
                .build();
    }

    public String makeSignature(HttpMethod method, String url, long timestamp) {
        String space = " ";
        String newLine = "\n";

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(clientSecret)
                .toString();

        try {
            SecretKeySpec signingKey = new SecretKeySpec(
                    clientSecret.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            String encodeBase64String = Base64.encodeBase64String(rawHmac);
            return encodeBase64String;
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating signature", e);
        }
    }

    @Bean
    public NcpHttpInterface ncpHttpInterface() {
        return HttpServiceProxyFactory.builderFor(
                        RestClientAdapter.create(ncpMapClient()))
                .build().createClient(NcpHttpInterface.class);
    }

    @Bean
    public NcpGeolocationInterface ncpGeolocationInterface() {
        return HttpServiceProxyFactory.builderFor(
                        RestClientAdapter.create(ncpGeoLocationClient()))
                .build().createClient(NcpGeolocationInterface.class);
    }
}
