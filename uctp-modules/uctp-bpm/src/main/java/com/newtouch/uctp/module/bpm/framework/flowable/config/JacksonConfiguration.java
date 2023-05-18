package com.newtouch.uctp.module.bpm.framework.flowable.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 参考链接：https://blog.csdn.net/weixin_43183496/article/details/127610651
 * @author helong
 * @date 2023/5/6 18:00
 */
@EnableWebMvc
@Configuration(proxyBeanMethods = false)
public class JacksonConfiguration implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
    {
        int oldSize = converters.size();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        /**
         * 序列换成json时,将所有的long变成string
            * 因为js中得数字类型不能包含所有的java long值
            */
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        // 反序列化时忽略多余字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(simpleModule);
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(mappingJackson2HttpMessageConverter);
        /**
         * 循环移动元素（解决问题：新添加的转换器必须放在转换器List集合中的最前面，否则不会生效，要么使用另一种方法重写转换器类）
         */
        Collections.rotate(converters, converters.size() - oldSize);
    }

    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime>
    {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            //gen.writeNumber(value.toInstant(ZoneOffset.of("+8")).toEpochMilli());
            //System.out.println("日期：" + value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            gen.writeNumber(value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
    }
}
