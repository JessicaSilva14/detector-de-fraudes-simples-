package br.com.deteccaofraudes.servico;

import br.com.deteccaofraudes.modelo.Transacao;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Lê um CSV no formato: id;clienteId;valor;dataHora (com cabeçalho). */
public class LeitorTransacoes {

    public List<Transacao> ler(Path caminho) throws IOException {
        List<String> linhas = Files.readAllLines(caminho, StandardCharsets.UTF_8);
        List<Transacao> transacoes = new ArrayList<>();

        for (int i = 1; i < linhas.size(); i++) { // i = 1 pula o cabeçalho
            String linha = linhas.get(i).trim();
            if (linha.isEmpty()) {
                continue;
            }
            try {
                String[] campos = linha.split(";");
                if (campos.length != 4) {
                    throw new IllegalArgumentException("esperados 4 campos, encontrados " + campos.length);
                }
                transacoes.add(new Transacao(
                        campos[0].trim(),
                        campos[1].trim(),
                        new BigDecimal(campos[2].trim()),
                        LocalDateTime.parse(campos[3].trim())));
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Linha " + (i + 1) + " inválida: " + e.getMessage(), e);
            }
        }
        return transacoes;
    }
}
