package br.com.umc.cyclecare.controllers;


import br.com.umc.cyclecare.models.DomainEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Controller extends AbstractController {
    public DomainEntity create(DomainEntity entity) {
        return facade.create(entity);
    }

    public DomainEntity read(DomainEntity entity) {
        return facade.read(entity);
    }

    public DomainEntity update(DomainEntity entity) {
        return facade.update(entity);
    }

    public void delete(DomainEntity entity) {
        facade.delete(entity);
    }

    public List<DomainEntity> listAll(DomainEntity entity) {
        return facade.listAll(entity);
    }
}
