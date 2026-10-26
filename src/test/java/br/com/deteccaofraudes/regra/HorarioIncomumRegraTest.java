package br.com.deteccaofraudes.regra;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

import static br.com.deteccaofraudes.regra.TesteUtil.tx;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HorarioIncomumRegraTest {
    private final HorarioIncomumRegra regra =
            new HorarioIncomumRegra(LocalTime.MIDNIGHT, LocalTime.of(5, 0), new BigDecimal("500"));

    @Test
    void valorAltoDeMadrugadaGeraAlerta() {
        assertTrue(regra.avaliar(tx("A", "600", 5, 3, 0, 0), List.of()).isPresent());
    }

    @Test
    void valorBaixoDeMadrugadaNaoGeraAlerta() {
        assertFalse(regra.avaliar(tx("A", "100", 5, 3, 0, 0), List.of()).isPresent());
    }

    @Test
    void valorAltoDuranteODiaNaoGeraAlerta() {
        assertFalse(regra.avaliar(tx("A", "900", 5, 14, 0, 0), List.of()).isPresent());
    }
}
