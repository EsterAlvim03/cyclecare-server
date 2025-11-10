package br.com.umc.cyclecare.strategies;

import br.com.umc.cyclecare.models.DomainEntity;
import br.com.umc.cyclecare.models.Term;

public class ValidateTerm implements IStrategy {
    @Override
    public String execute(DomainEntity entity) {
        StringBuilder sb = new StringBuilder();

        if (entity == null) {
            sb.append("Entidade nula.");
            return sb.toString();
        }

        Term term = (Term) entity;

        if (term.getTerms() == null) {
            sb.append("Os termos são obrigatórios;\n");
        }

        return sb.toString();
    }
}
