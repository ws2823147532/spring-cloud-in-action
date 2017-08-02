package com.didispace.web;

import com.didispace.utils.URLUtil;
import com.google.common.collect.Maps;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "helloFallback")
    public Result sendEmail() {

        Map<String, Object> params = Maps.newHashMap();
        params.put("subject", "报表数据");
        params.put("content", "这是本季度的报表");
        params.put("address", "$fengkong$");
        params.put("duplicate", "$test$;swangJNI@gmail.com");
        params.put("bcc", "wsgeek@outlook.com");

        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("files", new FileSystemResource(new File("D:\\documents\\文档\\31301\\SpringCloudBook-master\\email-consumer\\pom.xml")));
        multiValueMap.add("files", new FileSystemResource(new File("D:\\documents\\文档\\31301\\SpringCloudBook-master\\email-consumer\\eureka-client.iml")));

//        HttpHeaders headers = new HttpHeaders();
//        MediaType type = new MediaType("multipart", "form-data", StandardCharsets.UTF_8);
//        headers.setContentType(type);
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        return restTemplate.postForObject(
                URLUtil.append("http://EMAIL-SERVICE/mailService/sendEmail", params),
                multiValueMap,
                Result.class);

    }

    @HystrixCommand(fallbackMethod = "helloFallback")
    public String sendTemplateEmail() {
        return restTemplate.getForEntity("http://HELLO-SERVICE/hello", String.class).getBody();
    }

    public Result helloFallback() {
        return Result.builder().data(null).msg("access failed").result(100).build();
    }



}
