package com.dh.goyeon.config;


import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.dh.goyeon.auth.KakaoAPI;
import com.mongodb.client.MongoClients;

@Configuration
@PropertySource("classpath:application.properties")  // 외부 설정 파일을 통해 DB 정보 관리
public class Config {

    // MongoDB 설정
    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(MongoClients.create("mongodb://localhost:27017"), "studydb");
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl("jdbc:mariadb://localhost:3306/goyeon");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");
        return dataSource;
    }

    
    // Spring JDBC 설정
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // MyBatis SqlSessionFactory 및 SqlSessionTemplate 설정
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        try {
            sessionFactory.setConfigLocation(new ClassPathResource("sql-map-config.xml"));
            sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mappings/*.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessionFactory;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactoryBean sqlSessionFactory) throws Exception {
        SqlSessionFactory sqlSessionFactoryObject = sqlSessionFactory.getObject();
        if (sqlSessionFactoryObject == null) {
            throw new Exception("SqlSessionFactory is not created.");
        }
        return new SqlSessionTemplate(sqlSessionFactoryObject);
    }

    // Transaction 설정
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    // MultipartResolver 설정
	/*
	 * @Bean public CommonsMultipartResolver multipartResolver() {
	 * CommonsMultipartResolver resolver = new CommonsMultipartResolver();
	 * resolver.setMaxUploadSize(100000000); resolver.setMaxInMemorySize(100000000);
	 * return resolver; }
	 */
    // Kakao API 설정
    @Bean
    public KakaoAPI kakaoAPI() {
        return new KakaoAPI("0f75d50a0d2954d94e730dadb55f2e15", "http://localhost:8080/kakao-login", "LQqvOycqeeFZGSiUPivF1JryuSS1siPA");
    }


    // Gmail 설정
    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("dlaehdgus000@korea.ac.kr");
        mailSender.setPassword("arvb ciae tlgp pyui");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        return mailSender;
    }
}
