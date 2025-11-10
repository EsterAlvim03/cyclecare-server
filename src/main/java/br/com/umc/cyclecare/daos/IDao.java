package br.com.umc.cyclecare.daos;


import br.com.umc.cyclecare.models.DomainEntity;

import java.util.List;

public interface IDao {
    DomainEntity create(DomainEntity entity);

    DomainEntity read(DomainEntity entity);

    DomainEntity update(DomainEntity entity);

    void delete(DomainEntity entity);

    List<DomainEntity> listAll(DomainEntity entity);
}
