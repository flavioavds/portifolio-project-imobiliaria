package com.portifolio.imobiliaria.valid;

public class ValidationCPF_CNPJ {
	
	public boolean isValidCPF(String cpf) {
        cpf = cpf.replaceAll("[^\\d]+", "");

        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais
        boolean todosDigitosIguais = true;
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                todosDigitosIguais = false;
                break;
            }
        }
        if (todosDigitosIguais) {
            return false;
        }

        // Verifica os dígitos verificadores
        try {
            int[] cpfNumeros = new int[11];
            for (int i = 0; i < 11; i++) {
                cpfNumeros[i] = Integer.parseInt(cpf.substring(i, i+1));
            }

            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += cpfNumeros[i] * (10 - i);
            }

            int resto = soma % 11;
            int digitoVerificador1 = resto < 2 ? 0 : 11 - resto;

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += cpfNumeros[i] * (11 - i);
            }

            resto = soma % 11;
            int digitoVerificador2 = resto < 2 ? 0 : 11 - resto;

            return cpfNumeros[9] == digitoVerificador1 && cpfNumeros[10] == digitoVerificador2;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isValidCNPJ(String cnpj) {
        if (cnpj.length() != 14) {
            return false;
        }

        int soma = 0;
        int dig = 0;

        cnpj.substring(0, 12);

        char[] cnpjCharArray = cnpj.toCharArray();
        for (int i = 0; i < 4; i++) {
            if (cnpjCharArray[i] - 48 >= 0 && cnpjCharArray[i] - 48 <= 9) {
                soma += (cnpjCharArray[i] - 48) * (6 - (i + 1));
            }
        }

        for (int i = 0; i < 8; i++) {
            if (cnpjCharArray[i + 4] - 48 >= 0 && cnpjCharArray[i + 4] - 48 <= 9) {
                soma += (cnpjCharArray[i + 4] - 48) * (10 - (i + 1));
            }
        }

        dig = 11 - (soma % 11);

        if (dig > 9) {
            dig = 0;
        }

        if (cnpjCharArray[12] - 48 != dig) {
            return false;
        }

        soma = 0;
        for (int i = 0; i < 5; i++) {
            if (cnpjCharArray[i] - 48 >= 0 && cnpjCharArray[i] - 48 <= 9) {
                soma += (cnpjCharArray[i] - 48) * (7 - (i + 1));
            }
        }

        for (int i = 0; i < 8; i++) {
            if (cnpjCharArray[i + 5] - 48 >= 0 && cnpjCharArray[i + 5] - 48 <= 9) {
                soma += (cnpjCharArray[i + 5] - 48) * (10 - (i + 1));
            }
        }

        dig = 11 - (soma % 11);

        if (dig > 9) {
            dig = 0;
        }

        if (cnpjCharArray[13] - 48 != dig) {
            return false;
        }

        return true;
    }

}
