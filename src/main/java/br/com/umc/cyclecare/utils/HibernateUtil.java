package br.com.umc.cyclecare.utils;


import br.com.umc.cyclecare.models.Cycle;
import br.com.umc.cyclecare.models.Term;
import br.com.umc.cyclecare.models.User;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory;

    static {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .build();
        try {
            sessionFactory = new MetadataSources(registry)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Cycle.class)
                    .addAnnotatedClass(Term.class)
                    .buildMetadata()
                    .buildSessionFactory();

        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            throw new ExceptionInInitializerError("Erro ao inicializar o Hibernate: " + e);
        }
    }
}
