package config;

import com.zaxxer.hikari.HikariDataSource;
import domain.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
@AllArgsConstructor
@Getter
public class HibernateFactory {
    private final static SessionFactory sessionFactory;

    static {
        HikariDataSource hikariDataSource = new HikariDataSource();

        hikariDataSource.setJdbcUrl(DbConfig.getUrl());
        hikariDataSource.setUsername(DbConfig.getUsername());
        hikariDataSource.setPassword(DbConfig.getPassword());

        hikariDataSource.setMaximumPoolSize(HikariCpConfig.getMaximumPoolSize());
        hikariDataSource.setMinimumIdle(HikariCpConfig.getMinimumIdle());
        hikariDataSource.setIdleTimeout(HikariCpConfig.getConnectionTimeout());
        hikariDataSource.setDriverClassName(HikariCpConfig.getDriverClassName());

        Configuration configuration = new Configuration();
        configuration.getProperties().put("hibernate.connection.datasource", hikariDataSource);
        configuration.getProperties().put("hibernate.dialect", HibernateConfig.getDialect());
        configuration.getProperties().put("hibernate.hbm2ddl.auto", HibernateConfig.getHbm2ddlAuto());
        configuration.getProperties().put("hibernate.show_sql", HibernateConfig.isShowSql());
        configuration.getProperties().put("hibernate.format_sql", HibernateConfig.isFormatSql());


        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        try {
            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(Exercise.class)
                    .addAnnotatedClass(Workout.class)
                    .addAnnotatedClass(WorkoutExercise.class)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Food.class)
                    .addAnnotatedClass(UserFood.class)
                    .buildMetadata()
                    .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}