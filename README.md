# Detector de Fraudes

Aplicação de console em Java que analisa transações financeiras de um arquivo CSV e sinaliza as que parecem suspeitas, usando regras de negócio simples e extensíveis.

## Regras implementadas
| Regra | O que detecta |
|---|---|
| Valor acima da média | Transação muito maior que a média histórica do cliente (padrão: 5x, com no mínimo 3 transações anteriores) |
| Transações em sequência | Mais de 3 transações do mesmo cliente em 5 minutos |
| Horário incomum | Transação de R$ 500 ou mais entre 00:00 e 05:00 |

## Tecnologias
- Java 17 (records, Optional, Streams básicos)
- Maven
- JUnit 5

## Como rodar
**Pelo VS Code:** abra a pasta `detector-fraudes`, abra `Main.java` e clique em **Run**.

**Pelo terminal:**
```bash
mvn compile exec:java
```
Para analisar outro arquivo: `mvn compile exec:java -Dexec.args="caminho/arquivo.csv"`

**Testes:**
```bash
mvn test
```

## Formato do CSV
```
id;clienteId;valor;dataHora
T001;C001;50.00;2026-10-01T10:15:00
```

## Decisões técnicas
- Cada regra implementa a interface `RegraFraude`: para criar uma regra nova basta uma nova classe, sem mexer no detector (princípio aberto/fechado).
- As transações são ordenadas por data antes da análise, para que o histórico de cada cliente contenha só o passado.
- O histórico é entregue às regras como lista somente leitura, evitando efeitos colaterais.
- `BigDecimal` para valores monetários.
- Parâmetros das regras (fator, janela, valor mínimo) são configuráveis no construtor.

## Estrutura
```
modelo/   -> Transacao, Alerta (records)
regra/    -> RegraFraude (interface) e as implementações
servico/  -> DetectorFraudes, LeitorTransacoes
util/     -> Moeda
```

## Próximos passos
- Ler os parâmetros das regras de um arquivo de configuração
- Salvar os alertas em banco de dados (JDBC)
- Nova regra: transação em cidade/país diferente da usual
- Exportar relatório dos alertas em CSV
