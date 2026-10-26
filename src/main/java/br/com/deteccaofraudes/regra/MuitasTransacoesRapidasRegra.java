package br.com.deteccaofraudes.regra;

import br.com.deteccaofraudes.modelo.Transacao;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MuitasTransacoesRapidasRegra implements RegraFraude {
    private final int maxTransacoes;
    private final Duration janela;

    public MuitasTransacoesRapidasRegra(int maxTransacoes, Duration janela) {
        this.maxTransacoes = maxTransacoes;
        this.janela = janela;
    }

    @Override
    public String nome() {
        return "Transações em sequência";
    }

    @Override
    public Optional<String> avaliar(Transacao atual, List<Transacao> historico) {
        LocalDateTime inicioJanela = atual.dataHora().minus(janela);

        int quantidade = 1; // a própria transação atual
        for (Transacao t : historico) {
            if (!t.dataHora().isBefore(inicioJanela)) {
                quantidade++;
            }
        }

        if (quantidade > maxTransacoes) {
            return Optional.of(String.format("%d transações em %d minutos (limite: %d)",
                    quantidade, janela.toMinutes(), maxTransacoes));
        }
        return Optional.empty();
    }
}
