package com.github.hong.common.config;

import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.OracleKeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.github.hong.common.aspect.RedissonLockAspect;
import com.github.hong.common.aspect.RetryAspect;
import com.github.hong.common.thread.ThreadConfigProperties;
import com.github.hong.common.thread.ThreadPoolTaskExecutorWrapper;
import com.github.hong.common.undertow.UndertowServerFactoryCustomizer;
import com.github.hong.core.utils.DateUtil;
import com.github.hong.core.utils.SpringBeanUtil;
import com.github.hong.core.web.filter.RequestBodyFilter;
import io.undertow.Undertow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.servlet.Filter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 抽象配置类
 *
 * @author hanson
 * @since 2023/6/5
 */
@Slf4j
public abstract class BaseConfig {


    /**
     * 构建redis消息监听容器
     * 需要打开redis
     * 配置项 rename-command CONFIG "" 需要注释掉改配置项 可选
     * （如果实在需要开启，则需要再实现类中禁用改方法的调用 ，详情见 ls.ov.rms.auth.context.listener.RedisKeyExpirationListener）
     * 配置项 notify-keyspace-events Ex 必须
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        return container;
    }


    /**
     * serializerByType 解决json中返回的 LocalDateTime 格式问题
     * deserializerByType 解决string类型入参转为 LocalDateTime 格式问题
     *
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> builder
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtil.DEFAULT_DATE_TIME_FORMAT)))
                .serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateUtil.DEFAULT_DATE_FORMAT)))
                .serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateUtil.DEFAULT_TIME_FORMAT)))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtil.DEFAULT_DATE_TIME_FORMAT)))
                .deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateUtil.DEFAULT_DATE_FORMAT)))
                .deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateUtil.DEFAULT_TIME_FORMAT)));
    }


    /**
     * 解决序列化和反序列化时，参数转换问题
     * addSerializer：序列化 （Controller 返回 给前端的json）
     * Long -> string
     * BigInteger -> string
     * BigDecimal -> string
     * date -> string
     * <p>
     * addDeserializer: 反序列化 （前端调用接口时，传递到后台的json）
     * 枚举类型: {"code": "xxx"} -> Enum
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        builder.simpleDateFormat(DateUtil.DEFAULT_DATE_TIME_FORMAT);
        ObjectMapper objectMapper = builder.createXmlMapper(false)
                // 取消默认转换timestamps形式
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                //.timeZone(TimeZone.getTimeZone("Asia/Shanghai"))
                .build();

        objectMapper
                // 如果java对象的属性为NULL则不参与序列化，即java对象序列化后的json串里不出现属性为null的字段。
                // 该功能可以使用@JsonInclude注解，也可以设置objectMapper属性。
                // 关闭属性为NULL还序列化功能，默认是开启
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                // 设置地理位置信息
                .setLocale(Locale.CHINA)
                // 默认情况下ObjectMapper序列化没有属性的空对象时会抛异常。可以通过SerializationFeature.FAIL_ON_EMPTY_BEANS设置当对象没有属性时，让其序列化能成功，不抛异常。
                // 对象属性为空时，默认序列化会失败
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                // 默认情况下ObjectMapper序列化没有属性的空对象时会抛异常。可以通过SerializationFeature.FAIL_ON_EMPTY_BEANS设置当对象没有属性时，让其序列化能成功，不抛异常。
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // 该特性决定parser是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）。
                // 如果该属性关闭，则如果遇到这些字符，则会抛出异常。JSON标准说明书要求所有控制符必须使用引号，因此这是一个非标准的特性
                .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true)
                // 设置解析能识别JSON串里的注释符
                // 当反序列化的JSON串里带有反斜杠时，默认objectMapper反序列化会失败，抛出异常Unrecognized character escape。
                // 可以通过Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER来设置当反斜杠存在时，能被objectMapper反序列化。
                .configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true)
                // 当json字符串里带注释符时，默认情况下解析器不能解析。Feature.ALLOW_COMMENTS特性决定解析器是否允许解析使用Java/C++ 样式的注释（包括'/'+'*' 和'//' 变量）。
                .configure(JsonReadFeature.ALLOW_JAVA_COMMENTS.mappedFeature(), true)
                //parser解析器默认情况下不能识别单引号包住的属性和属性值，默认下该属性也是关闭的。需要设置JsonParser.Feature.ALLOW_SINGLE_QUOTES为true。
                .configure(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature(), true)
                //日期格式
                .setDateFormat(new SimpleDateFormat(DateUtil.DEFAULT_DATE_TIME_FORMAT));

        //反序列化时，属性不存在的兼容处理
        objectMapper
                .getDeserializationConfig()
                // 忽略空bean转json的错误
                .withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        SimpleModule simpleModule = new SimpleModule()
                .addSerializer(Long.class, ToStringSerializer.instance)
                .addSerializer(Long.TYPE, ToStringSerializer.instance)
                .addSerializer(BigInteger.class, ToStringSerializer.instance)
                .addSerializer(BigDecimal.class, ToStringSerializer.instance);

        return objectMapper.registerModule(simpleModule);
    }


    @Bean
    public ThreadConfigProperties threadConfigProperties() {
        return new ThreadConfigProperties();
    }

    /**
     * 功能描述: 自定义异步执行器
     * 不要命名为 taskScheduler，与spring框架的bean重名
     * 用于处理系统执行日志 ，对于量大的业务需要单独再创建一个自定义异步执行器来处理
     * 20, 300, 10, 60  该配置： 同一时间最大并发量300，（已经验证通过， 商户都可以收到请求消息）
     * 缓存队列尽量减少，否则将堵塞在队列中无法执行。
     * corePoolSize 根据机器的配置进行添加。此处设置的为20
     *
     * @return 异步执行器
     */
    @Bean(name = "hongAsyncExecutor")
    public Executor hongAsyncExecutor(
            ThreadConfigProperties properties) {
        //SpringBoot项目，可使用Spring提供的对 ThreadPoolExecutor 封装的线程池 ThreadPoolTaskExecutor：
        // ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //自定义ThreadPoolTaskExecutor，会打印线程池情况
        ThreadPoolTaskExecutorWrapper executor = new ThreadPoolTaskExecutorWrapper();
        //配置核心线程数
        executor.setCorePoolSize(properties.getCorePoolSize());
        //配置最大线程数
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        //配置队列大小
        executor.setQueueCapacity(properties.getQueueCapacity());
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(properties.getNamePrefix());
        /*
         * 当达到最大线程数时如何处理新任务
         * AbortPolicy:丢弃任务并抛出RejectedExecutionException异常
         * DiscardPolicy:丢弃任务，但是不抛出异常。如果线程队列已满，则后续提交的任务都会被丢弃，且是静默丢弃
         * DiscardOldestPolicy:丢弃队列最前面的任务，然后重新提交被拒绝的任务
         * CallerRunsPolicy:由调用线程处理该任务
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 允许的空闲时间
        executor.setKeepAliveSeconds(properties.getKeepAliveSeconds());
        //执行初始化
        executor.initialize();
        return executor;
    }


    @Bean
    @ConditionalOnMissingBean(RetryAspect.class)
    public RetryAspect retryAspect() {
        return new RetryAspect();
    }

    @Bean
    @ConditionalOnMissingBean(RedissonLockAspect.class)
    public RedissonLockAspect redissonLockAspect() {
        return new RedissonLockAspect();
    }

    @Bean
    @ConditionalOnMissingBean(SpringBeanUtil.class)
    public SpringBeanUtil getSpringUtils(ApplicationContext applicationContext) {
        SpringBeanUtil.setApplicationContext(applicationContext);
        return new SpringBeanUtil();
    }


    /**
     * 基于ORACLE 连接驱动
     *
     * @return 返回序列生成方式
     */
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.driver-class-name", havingValue = "oracle.jdbc.OracleDriver", matchIfMissing = true)
    @ConditionalOnMissingBean(OracleKeyGenerator.class)
    public IKeyGenerator oracleKeyGenerator() {
        return new OracleKeyGenerator();
    }


    /**
     * 定制 Undertow 服务
     *
     * @return UndertowServerFactoryCustomizer
     */
    @Bean
    @ConditionalOnClass(Undertow.class)
    public UndertowServerFactoryCustomizer getUndertowServerFactoryCustomizer() {
        return new UndertowServerFactoryCustomizer();
    }


    /**
     * redis模板类
     *
     * @param factory redis链接工厂
     * @return redis模板类
     */
    @Bean(name = {"redisTemplate", "stringRedisTemplate"})
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory,
                                                   ObjectMapper jacksonObjectMapper) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        // 配置连接工厂
        redisTemplate.setConnectionFactory(factory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<Object> jacksonSeial = new Jackson2JsonRedisSerializer<>(Object.class);

        //ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        // om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        jacksonSeial.setObjectMapper(jacksonObjectMapper);

        // 值采用json序列化
        redisTemplate.setValueSerializer(jacksonSeial);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置hash key 和value序列化模式
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jacksonSeial);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //1、多租户插件
        //TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor();
        //interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
        //2、动态表名插件
        //DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        //interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        //3、分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(5000L);//限制最大的分页条数
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        /*4、乐观锁插件，实体类上需要有以下内容
         * @Version
         * private Integer version;
         */
        //OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor = new OptimisticLockerInnerInterceptor();
        //interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor);
        //5、sql 性能规范插件
        //  IllegalSQLInnerInterceptor illegalSQLInnerInterceptor = new IllegalSQLInnerInterceptor();
        //  interceptor.addInnerInterceptor(illegalSQLInnerInterceptor);
        //6、防止全表更新与删除插件
        //  BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
        //  interceptor.addInnerInterceptor(blockAttackInnerInterceptor);
        return interceptor;
    }


    /**
     * 实现可重复读Request
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean<Filter> addRequestBodyFilter() {
        RequestBodyFilter requestBodyFilter = new RequestBodyFilter();
        FilterRegistrationBean<Filter> registrationBean
                = new FilterRegistrationBean<>();
        registrationBean.setFilter(requestBodyFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Integer.MAX_VALUE);
        return registrationBean;
    }
}
