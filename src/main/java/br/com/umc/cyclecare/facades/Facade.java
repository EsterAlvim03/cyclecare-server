package br.com.umc.cyclecare.facades;


import br.com.umc.cyclecare.daos.CycleDao;
import br.com.umc.cyclecare.daos.IDao;
import br.com.umc.cyclecare.daos.TermDao;
import br.com.umc.cyclecare.daos.UserDao;
import br.com.umc.cyclecare.models.Cycle;
import br.com.umc.cyclecare.models.DomainEntity;
import br.com.umc.cyclecare.models.Term;
import br.com.umc.cyclecare.models.User;
import br.com.umc.cyclecare.strategies.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Facade implements IFacade {
    private Map<String, IDao> daos;
    private Map<String, List<IStrategy>> strategies;

    public Facade() {
        setStrategies();
        setDaos();
    }

    private void setStrategies() {
        strategies = new HashMap<>();

        ValidateCPF validateCPF = new ValidateCPF();
        ValidateCycle validateCycle = new ValidateCycle();
        ValidateTerm validateTerm = new ValidateTerm();
        ValidateUser validateUser = new ValidateUser();

        List<IStrategy> strategiesCycle = new ArrayList<>();
        strategiesCycle.add(validateCycle);
        strategies.put(Cycle.class.getName(), strategiesCycle);

        List<IStrategy> strategiesTerm = new ArrayList<>();
        strategiesTerm.add(validateTerm);
        strategies.put(Term.class.getName(), strategiesTerm);

        List<IStrategy> strategiesUser = new ArrayList<>();
        strategiesUser.add(validateCPF);
        strategiesUser.add(validateUser);
        strategies.put(User.class.getName(), strategiesUser);
    }

    private void setDaos() {
        daos = new HashMap<>();

        daos.put(Cycle.class.getName(), new CycleDao());
        daos.put(Term.class.getName(), new TermDao());
        daos.put(User.class.getName(), new UserDao());
    }

    public String execute(DomainEntity entity) {
        String className = entity.getClass().getName();

        List<IStrategy> strategyEntity = strategies.get(className);

        StringBuilder sb = new StringBuilder();
        for (IStrategy strategy : strategyEntity) {
            String msg = strategy.execute(entity);
            if (msg != null) {
                sb.append(msg);
            }
        }

        if (!sb.isEmpty()) {
            return sb.toString();
        } else {
            return null;
        }
    }

    @Override
    public DomainEntity create(DomainEntity entity) {
        String key = entity.getClass().getName();
        String msg = execute(entity);

        if (msg == null) {
            IDao dao = daos.get(key);
            return dao.create(entity);
        } else {
            throw new RuntimeException(msg);
        }
    }

    @Override
    public DomainEntity read(DomainEntity entity) {
        String key = entity.getClass().getName();
        IDao dao = daos.get(key);
        return dao.read(entity);
    }

    @Override
    public DomainEntity update(DomainEntity entity) {
        String key = entity.getClass().getName();
        IDao dao = daos.get(key);
        return dao.update(entity);
    }

    @Override
    public void delete(DomainEntity entity) {
        String key = entity.getClass().getName();
        IDao dao = daos.get(key);
        dao.delete(entity);
    }

    @Override
    public List<DomainEntity> listAll(DomainEntity entity) {
        String key = entity.getClass().getName();
        IDao dao = daos.get(key);
        return dao.listAll(entity);
    }
}
