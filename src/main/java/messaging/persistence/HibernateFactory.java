package messaging.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Map;
import java.util.Set;

public class HibernateFactory {
    private static HibernateFactory hibernateFactory;
    private static volatile boolean alive;
    private static StandardServiceRegistry registry;
    private SessionFactory sessionFactory;
    private Map<String, String> settings;
    private Set<Class<?>> models;

    private HibernateFactory() {
        alive = true;
    }


    public static HibernateFactory getHibernateFactory() {
        HibernateFactory factory;
        if(!alive){
            createFactory();
        }
        factory = hibernateFactory;
        return factory;
    }



    private static void createFactory() {
        hibernateFactory = new HibernateFactory();
        hibernateFactory.getSessionFactory();
    }

    private SessionFactory initSessionFactory() {
        if (this.sessionFactory == null) {
            try {
                HibernateSettings settings = new HibernateSettings();
                StandardServiceRegistryBuilder registryBuilder =
                        new StandardServiceRegistryBuilder();

                registryBuilder.applySettings(settings.getHibernateSettings());

                registry = registryBuilder.build();

                MetadataSources sources = new MetadataSources(registry);
                settings.getModels().forEach(x->sources.addAnnotatedClass(x));

                Metadata metadata = sources.getMetadataBuilder().build();

                this.sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                System.out.println("SessionFactory creation failed");
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    SessionFactory getSessionFactory() {
        return initSessionFactory();
    }

    public void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
