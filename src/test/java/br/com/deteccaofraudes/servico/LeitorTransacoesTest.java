package br.com.deteccaofraudes.servico;

import br.com.deteccaofraudes.modelo.Transacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LeitorTransacoesTest {

    @TempDir
    Path pasta;

    @Test
    void leArquivoValido() throws IOException {
        Path arquivo = pasta.resolve("t.csv");
        Files.writeString(arquivo, "id;clienteId;valor;dataHora\nT1;C1;50.00;2026-10-01T10:15:00\n");

        List<Transacao> lidas = new LeitorTransacoes().ler(arquivo);

        assertEquals(1, lidas.size());
        assertEquals("T1", lidas.get(0).id());
    }

    @Test
    void linhaInvalidaLancaExcecaoComNumeroDaLinha() throws IOException {
        Path arquivo = pasta.resolve("t.csv");
        Files.writeString(arquivo, "id;clienteId;valor;dataHora\nT1;C1;abc;2026-10-01T10:15:00\n");

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new LeitorTransacoes().ler(arquivo));
        assertEquals(true, e.getMessage().contains("Linha 2"));
    }
}
