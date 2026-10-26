package br.com.deteccaofraudes.regra;

import br.com.deteccaofraudes.modelo.Transacao;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static br.com.deteccaofraudes.regra.TesteUtil.tx;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MuitasTransacoesRapidasRegraTest {
    private final MuitasTransacoesRapidasRegra regra =
            new MuitasTransacoesRapidasRegra(3, Duration.ofMinutes(5));

    @Test
    void quartaTransacaoEmCincoMinutosGeraAlerta() {
        List<Transacao> historico = List.of(
                tx("A", "10", 5, 14, 0, 0),
                tx("B", "10", 5, 14, 1, 0),
                tx("C", "10", 5, 14, 2, 0));
        assertTrue(regra.avaliar(tx("D", "10", 5, 14, 3, 0), historico).isPresent());
    }

    @Test
    void terceiraTransacaoAindaEhPermitida() {
        List<Transacao> historico = List.of(
                tx("A", "10", 5, 14, 0, 0),
                tx("B", "10", 5, 14, 1, 0));
        assertFalse(regra.avaliar(tx("C", "10", 5, 14, 2, 0), historico).isPresent());
    }

    @Test
    void transacoesEspacadasNaoGeramAlerta() {
        List<Transacao> historico = List.of(
                tx("A", "10", 5, 10, 0, 0),
                tx("B", "10", 5, 11, 0, 0),
                tx("C", "10", 5, 12, 0, 0));
        assertFalse(regra.avaliar(tx("D", "10", 5, 13, 0, 0), historico).isPresent());
    }
}
