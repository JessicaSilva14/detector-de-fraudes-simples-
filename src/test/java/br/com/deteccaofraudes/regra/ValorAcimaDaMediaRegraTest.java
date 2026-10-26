package br.com.deteccaofraudes.regra;

import br.com.deteccaofraudes.modelo.Transacao;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static br.com.deteccaofraudes.regra.TesteUtil.tx;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValorAcimaDaMediaRegraTest {
    private final ValorAcimaDaMediaRegra regra = new ValorAcimaDaMediaRegra(new BigDecimal("5"), 3);
    private final List<Transacao> historico = List.of(
            tx("A", "100", 1, 10, 0, 0),
            tx("B", "100", 2, 10, 0, 0),
            tx("C", "100", 3, 10, 0, 0));

    @Test
    void valorMuitoAcimaDaMediaGeraAlerta() {
        assertTrue(regra.avaliar(tx("D", "1000", 4, 10, 0, 0), historico).isPresent());
    }

    @Test
    void valorDentroDoNormalNaoGeraAlerta() {
        assertFalse(regra.avaliar(tx("D", "400", 4, 10, 0, 0), historico).isPresent());
    }

    @Test
    void semHistoricoSuficienteNaoGeraAlerta() {
        List<Transacao> curto = List.of(tx("A", "100", 1, 10, 0, 0));
        assertFalse(regra.avaliar(tx("D", "9999", 4, 10, 0, 0), curto).isPresent());
    }
}
