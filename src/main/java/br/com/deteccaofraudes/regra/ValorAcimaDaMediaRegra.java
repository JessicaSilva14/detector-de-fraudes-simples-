package br.com.deteccaofraudes.regra;

import br.com.deteccaofraudes.modelo.Transacao;
import br.com.deteccaofraudes.util.Moeda;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class ValorAcimaDaMediaRegra implements RegraFraude {
    private final BigDecimal fator;
    private final int minimoHistorico;

    public ValorAcimaDaMediaRegra(BigDecimal fator, int minimoHistorico) {
        this.fator = fator;
        this.minimoHistorico = minimoHistorico;
    }

    @Override
    public String nome() {
        return "Valor acima da média";
    }

    @Override
    public Optional<String> avaliar(Transacao atual, List<Transacao> historico) {
        // Sem histórico suficiente não dá para saber o que é "normal" para o cliente.
        if (historico.size() < minimoHistorico) {
            return Optional.empty();
        }

        BigDecimal soma = BigDecimal.ZERO;
        for (Transacao t : historico) {
            soma = soma.add(t.valor());
        }
        BigDecimal media = soma.divide(BigDecimal.valueOf(historico.size()), 2, RoundingMode.HALF_EVEN);
        BigDecimal limite = media.multiply(fator);

        if (atual.valor().compareTo(limite) > 0) {
            return Optional.of(String.format("Valor de %s é mais de %sx a média do cliente (%s)",
                    Moeda.formatar(atual.valor()), fator.stripTrailingZeros().toPlainString(),
                    Moeda.formatar(media)));
        }
        return Optional.empty();
    }
}
