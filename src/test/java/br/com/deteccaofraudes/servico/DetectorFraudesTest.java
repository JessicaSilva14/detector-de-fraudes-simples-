package br.com.deteccaofraudes.servico;

import br.com.deteccaofraudes.modelo.Alerta;
import br.com.deteccaofraudes.modelo.Transacao;
import br.com.deteccaofraudes.regra.ValorAcimaDaMediaRegra;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DetectorFraudesTest {

    private Transacao tx(String id, String cliente, String valor, int dia) {
        return new Transacao(id, cliente, new BigDecimal(valor), LocalDateTime.of(2026, 10, dia, 10, 0));
    }

    private final DetectorFraudes detector =
            new DetectorFraudes(List.of(new ValorAcimaDaMediaRegra(new BigDecimal("5"), 3)));

    @Test
    void marcaApenasATransacaoSuspeita() {
        List<Transacao> transacoes = List.of(
                tx("T1", "C1", "100", 1),
                tx("T2", "C1", "100", 2),
                tx("T3", "C1", "100", 3),
                tx("T4", "C1", "1000", 4));

        List<Alerta> alertas = detector.analisar(transacoes);

        assertEquals(1, alertas.size());
        assertEquals("T4", alertas.get(0).transacao().id());
    }

    @Test
    void naoMisturaHistoricoDeClientesDiferentes() {
        List<Transacao> transacoes = List.of(
                tx("T1", "C1", "100", 1),
                tx("T2", "C1", "100", 2),
                tx("T3", "C1", "100", 3),
                tx("T4", "C2", "1000", 4)); // C2 não tem histórico

        assertTrue(detector.analisar(transacoes).isEmpty());
    }

    @Test
    void funcionaMesmoComTransacoesForaDeOrdem() {
        List<Transacao> transacoes = List.of(
                tx("T4", "C1", "1000", 4),
                tx("T2", "C1", "100", 2),
                tx("T1", "C1", "100", 1),
                tx("T3", "C1", "100", 3));

        List<Alerta> alertas = detector.analisar(transacoes);

        assertEquals(1, alertas.size());
        assertEquals("T4", alertas.get(0).transacao().id());
    }
}
