package br.com.deteccaofraudes.regra;

import br.com.deteccaofraudes.modelo.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class TesteUtil {

    private TesteUtil() {
    }

    public static Transacao tx(String id, String valor, int dia, int hora, int minuto, int segundo) {
        return new Transacao(id, "C1", new BigDecimal(valor),
                LocalDateTime.of(2026, 10, dia, hora, minuto, segundo));
    }
}
