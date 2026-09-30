# Web Crawler ACZG - Padrão TISS / ANS

Desafio de estágio: crawler para coletar dados públicos do Padrão TISS da Agência Nacional de Saúde Suplementar (ANS).

**Autor:** Myke William

## O que faz

O bot executa três tarefas:

1. **Task 1** — Baixa o Componente de Comunicação do Padrão TISS, na versão mais recente. Descobre a versão atual dinamicamente, sem URL hardcoded.
2. **Task 2** — Coleta o histórico das versões dos componentes do Padrão TISS (competência, publicação e início de vigência) a partir de jan/2016, e salva em CSV.
3. **Task 3** — Baixa a Tabela de erros no envio para a ANS (.xlsx).

## Tecnologias

- Groovy 4
- Gradle Wrapper (não requer Gradle instalado)
- Jsoup (parsing HTML e download de arquivos)
- Java 21

## Como rodar

```bash
./gradlew run --no-configuration-cache