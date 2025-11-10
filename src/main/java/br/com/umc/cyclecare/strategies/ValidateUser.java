package br.com.umc.cyclecare.strategies;

import br.com.umc.cyclecare.models.DomainEntity;
import br.com.umc.cyclecare.models.User;

public class ValidateUser implements IStrategy {
    @Override
    public String execute(DomainEntity entity) {
        StringBuilder sb = new StringBuilder();

        if (entity == null) {
            sb.append("Entidade nula.");
            return sb.toString();
        }

        User user = (User) entity;

        if (user.getName() == null) {
            sb.append("Nome é obrigatório;\n");
        }

        if (user.getEmail() == null) {
            sb.append("E-mail é obrigatório;\n");
        }

        if (user.getPhone() == null) {
            sb.append("Telefone é obrigatório;\n");
        }

        if (user.getPassword() == null) {
            sb.append("Senha é obrigatória;");
        } else if (user.getPassword().length() < 8) {
            sb.append("A senha deve conter no mínimo 8 caracteres;");
        }

        return sb.toString();
    }
}
