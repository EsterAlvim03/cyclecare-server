package br.com.umc.cyclecare.strategies;

import br.com.umc.cyclecare.models.DomainEntity;
import br.com.umc.cyclecare.models.User;

public class ValidateCPF implements IStrategy {
    private static boolean isValid(String fullCpf) {
        String cpf = fullCpf.replaceAll("[^\\d]+", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sum = 0;
        for (int i = 1; i <= 9; i++) {
            sum += Integer.parseInt(cpf.substring(i - 1, i)) * (11 - i);
        }
        int rest = (sum * 10) % 11;
        if (rest == 10 || rest == 11) rest = 0;
        if (rest != Integer.parseInt(cpf.substring(9, 10))) {
            return false;
        }

        sum = 0;
        for (int i = 1; i <= 10; i++) {
            sum += Integer.parseInt(cpf.substring(i - 1, i)) * (12 - i);
        }
        rest = (sum * 10) % 11;
        if (rest == 10 || rest == 11) rest = 0;
        return rest == Integer.parseInt(cpf.substring(10, 11));
    }

    @Override
    public String execute(DomainEntity entity) {
        StringBuilder sb = new StringBuilder();

        User user = (User) entity;

        String cpf = user.getCpf();

        if (cpf == null) {
            sb.append("CPF é obrigatório.");
            return sb.toString();
        }

        if (!isValid(cpf)) {
            sb.append("CPF inválido;\n");
        }

        return sb.toString();
    }
}
