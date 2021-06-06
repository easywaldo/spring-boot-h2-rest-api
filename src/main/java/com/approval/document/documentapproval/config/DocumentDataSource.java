package com.approval.document.documentapproval.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Profile({"local"})
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = {"com.approval.document.documentapproval.domain"},
    entityManagerFactoryRef = "easyEntityManagerFactory",
    transactionManagerRef = "easyTransactionManagerFactory"
)
public class DocumentDataSource {

    @Value("${spring.datasource.driver-class-name}")
    private String easyDriverClassName;

    @Value("${spring.datasource.url}")
    private String easyConnUrl;

    @Value("${spring.datasource.username}")
    private String easyUserName;

    @Value("${spring.datasource.password}")
    private String easyUserPwd;

    @Bean(name = "easyDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.test.datasource")
    public DataSource easyDataSource() {
        return DataSourceBuilder.create()
            .driverClassName(easyDriverClassName)
            .url(easyConnUrl)
            .username(easyUserName)
            .password(easyUserPwd)
            .build();
    }

    @Primary
    @Bean(name="easyEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean easyEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(easyDataSource());
        entityManagerFactoryBean.setPackagesToScan("com.approval.document.documentapproval.domain");
        entityManagerFactoryBean.setPersistenceUnitName("easy-master");
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setJpaPropertyMap(hibernateProperties());
        entityManagerFactoryBean.afterPropertiesSet();
        return entityManagerFactoryBean;
    }

    @Primary
    @Bean(name = "easyTransactionManagerFactory")
    public PlatformTransactionManager hottBackOfficeTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(easyEntityManagerFactory().getObject());
        transactionManager.setDataSource(easyDataSource());

        return transactionManager;
    }

    private Map<String, Object> hibernateProperties(){
        Resource objResource = new ClassPathResource("hibernate.properties");
        try{
            Properties objProperties = PropertiesLoaderUtils.loadProperties(objResource);
            return objProperties.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue()));
        }catch(IOException objEx){
            return new HashMap<>();
        }
    }
}
