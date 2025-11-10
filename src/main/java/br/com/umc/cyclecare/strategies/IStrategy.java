package br.com.umc.cyclecare.strategies;

import br.com.umc.cyclecare.models.DomainEntity;

public interface IStrategy {
    public String execute(DomainEntity entity);
}