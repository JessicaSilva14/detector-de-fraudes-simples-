package br.com.deteccaofraudes.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class Moeda {

    private Moeda() {
    }

    public static String formatar(BigDecimal valor) {
        return NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR")).format(valor);
    }
}
