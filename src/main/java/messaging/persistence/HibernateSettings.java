package messaging.persistence;

import org.reflections.Reflections;

import javax.persistence.Entity;
import java.util.*;

class HibernateSettings {

    HibernateSettings() {

    }

     Set<Class<?>> getModels() {
        Reflections reflections = new Reflections(this.getClass().getPackageName());
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Entity.class);
        return classSet;
    }

     Map<String, String> getHibernateSettings() {
        Map<String, String> settings = new HashMap<>();
        settings.put("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        settings.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/messaging_db?createDatabaseIfNotExist=true");
        settings.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        settings.put("hibernate.connection.username", "root");
        settings.put("hibernate.connection.password", "root");
        settings.put("hibernate.show_sql", "true");
        settings.put("hibernate.hbm2ddl.auto", "update");
        return settings;
    }
}
