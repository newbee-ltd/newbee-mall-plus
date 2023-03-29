// package ltd.newbee.mall.config;
//
// import com.zaxxer.hikari.HikariDataSource;
// import org.apache.ibatis.session.SqlSessionFactory;
// import org.mybatis.spring.annotation.MapperScan;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.EnableAspectJAutoProxy;
// import org.springframework.context.annotation.Primary;
// import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
// import org.springframework.jdbc.datasource.DataSourceTransactionManager;
// import org.springframework.transaction.annotation.EnableTransactionManagement;
//
// import javax.sql.DataSource;
//
//
// @Configuration
// @EnableAspectJAutoProxy
// @EnableTransactionManagement
// @MapperScan(basePackages = "ltd.newbee.mall.dao", sqlSessionFactoryRef = "masterSqlSessionFactory")
// public class HikariConfig {
//
//
//     @Bean(name = "dataSource")
//     @ConfigurationProperties(prefix = "spring.datasource.hikari")
//     public DataSource masterDataSource() {
//         return new HikariDataSource();
//     }
//
//     /**
//      * @param datasource 数据源
//      * @return SqlSessionFactory
//      * @Primary 默认SqlSessionFactory
//      */
//     @Primary
//     @Bean(name = "masterSqlSessionFactory")
//     public SqlSessionFactory masterSqlSessionFactory(@Qualifier("dataSource") DataSource datasource) throws Exception {
//         SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//         bean.setDataSource(datasource);
//         // mybatis扫描xml所在位置
//         bean.setMapperLocations(new PathMatchingResourcePatternResolver()
//                 .getResources("classpath*:mapper/*.xml"));
//         bean.setTypeAliasesPackage("ltd.**.entity");
//         return bean.getObject();
//     }
//
//     @Bean(name = "masterTransactionManager")
//     public DataSourceTransactionManager masterTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
//         return new DataSourceTransactionManager(dataSource);
//     }
// }
