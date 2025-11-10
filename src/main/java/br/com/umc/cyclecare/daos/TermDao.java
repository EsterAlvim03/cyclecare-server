package br.com.umc.cyclecare.daos;

import br.com.umc.cyclecare.models.DomainEntity;
import br.com.umc.cyclecare.models.Term;
import br.com.umc.cyclecare.utils.HibernateUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TermDao implements IDao {
    @Override
    public DomainEntity create(DomainEntity entity) {
        try {
            Term term = (Term) entity;

            HibernateUtil.getSessionFactory().inTransaction(session -> {
                session.createMutationQuery("UPDATE Term t SET t.isActive = false WHERE t.isActive = true")
                        .executeUpdate();

                term.setActive(true);

                session.persist(term);
            });

            return term;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public DomainEntity read(DomainEntity entity) {
        try {
            Term term = (Term) entity;

            HibernateUtil.getSessionFactory().inTransaction(session -> {
                String hql = "FROM Term t WHERE t.isActive = true";
                Term dbTerm = session.createQuery(hql, Term.class)
                        .uniqueResult();

                if (dbTerm == null) {
                    throw new RuntimeException("Nenhum termo cadastrado");
                }

                term.setId(dbTerm.getId());
                term.setCreatedAt(dbTerm.getCreatedAt());
                term.setTerms(dbTerm.getTerms());
            });

            return term;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public DomainEntity update(DomainEntity entity) {
        return null;
    }

    @Override
    public void delete(DomainEntity entity) {

    }

    @Override
    public List<DomainEntity> listAll(DomainEntity entity) {
        return List.of();
    }
}