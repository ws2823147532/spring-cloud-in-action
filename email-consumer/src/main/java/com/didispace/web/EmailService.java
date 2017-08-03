package com.didispace.web;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class EmailService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 这里使用RestTemplate调用服务总是会出现中文乱码问题，试了两种解决方案：1、重写MessageConverter。2、设置头信息ContentType
     * 都不起作用。
     *
     * 于是想到了另一个办法：将包含中文的数据 URLEncoder.encode() 一下，在服务端只需 URLDecoder.decode() 即可。
     * @return
     */
    @HystrixCommand(fallbackMethod = "helloFallback")
    public Result sendEmail() {

//        Map<String, Object> params = Maps.newHashMap();
//        params.put("subject", "报表数据");
//        params.put("content", "这是本季度的报表");
//        params.put("address", "$fengkong$");
//        params.put("duplicate", "$test$;swangJNI@gmail.com");
//        params.put("bcc", "wsgeek@outlook.com");

        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("files", new FileSystemResource(new File("/Volumes/Ext/Workspace/spring-cloud-in-action/eureka-server.log")));
        multiValueMap.add("files", new FileSystemResource(new File("/Volumes/Ext/Workspace/spring-cloud-in-action/pom.xml")));
        try {
            multiValueMap.add("subject", URLEncoder.encode("报表数据", "UTF-8"));
            multiValueMap.add("content", URLEncoder.encode("这是本季度的报表", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        multiValueMap.add("address", "2823147532@qq.com");
        multiValueMap.add("duplicate", "swangJNI@gmail.com");
        multiValueMap.add("bcc", "shang.wang@renren-inc.com");

//        HttpHeaders headers = new HttpHeaders();
////        MediaType type = new MediaType("multipart", "form-data", StandardCharsets.UTF_8);
////        headers.setContentType(type);
//        headers.set("Accept-Charset", "utf-8");
//        headers.set("Content-type", "multipart/form-data; charset=utf-8");  //header的规定
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multiValueMap, headers);

        return restTemplate.postForObject(
                "http://EMAIL-SERVICE/mailService/sendEmail",
//                URLUtil.append("http://EMAIL-SERVICE/mailService/sendEmail", params),
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
