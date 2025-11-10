package br.com.umc.cyclecare.daos;

import br.com.umc.cyclecare.models.Cycle;
import br.com.umc.cyclecare.models.DomainEntity;
import br.com.umc.cyclecare.utils.HibernateUtil;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CycleDao implements IDao {
    @Override
    public DomainEntity create(DomainEntity entity) {
        try {
            Cycle cycle = (Cycle) entity;

            HibernateUtil.getSessionFactory().inTransaction(session -> {
                session.persist(cycle);
            });

            return cycle;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public DomainEntity read(DomainEntity entity) {
        try {
            Cycle cycle = (Cycle) entity;
            HibernateUtil.getSessionFactory().inTransaction(session -> {
                Cycle dbCycle = session.get(Cycle.class, cycle.getId());

                if (dbCycle == null) {
                    throw new RuntimeException("Ciclo não encontrado");
                }

                cycle.setMood(dbCycle.getMood());
                cycle.setStartDate(dbCycle.getStartDate());
                cycle.setEndDate(dbCycle.getEndDate());
            });
            return cycle;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public DomainEntity update(DomainEntity entity) {
        try {
            Cycle cycle = (Cycle) entity;
            HibernateUtil.getSessionFactory().inTransaction(session -> {
                Cycle dbCycle = session.get(Cycle.class, cycle.getId());

                if (dbCycle == null) {
                    throw new RuntimeException("Ciclo não encontrado");
                }

                dbCycle.setMood(cycle.getMood());
                dbCycle.setStartDate(cycle.getStartDate());
                dbCycle.setEndDate(cycle.getEndDate());
            });

            return cycle;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(DomainEntity entity) {
        try {
            Cycle cycle = (Cycle) entity;
            HibernateUtil.getSessionFactory().inTransaction(session -> {
                Cycle dbCycle = session.get(Cycle.class, cycle.getId());

                if (dbCycle == null) {
                    throw new RuntimeException("Ciclo não encotrado");
                }

                session.remove(dbCycle);
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }


    }

    @Override
    public List<DomainEntity> listAll(DomainEntity entity) {
        try {
            Cycle cycle = (Cycle) entity;

            return HibernateUtil.getSessionFactory().fromTransaction(session -> {
                String hql = "SELECT c FROM Cycle c WHERE c.user.id = :userId";

                List<Cycle> cycles = session.createQuery(hql, Cycle.class)
                        .setParameter("userId", cycle.getUser().getId())
                        .getResultList();

                return new ArrayList<>(cycles);
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
