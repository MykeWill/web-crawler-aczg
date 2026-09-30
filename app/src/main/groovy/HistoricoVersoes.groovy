import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class HistoricoVersoes {

    static final String URL_HISTORICO =
            "https://www.gov.br/ans/pt-br/assuntos/prestadores/" +
                    "padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss/" +
                    "padrao-tiss-historico-das-versoes-dos-componentes-do-padrao-tiss"

    static final File ARQUIVO_SAIDA = new File("../Downloads/historico_versoes_tiss.csv")

    static void coletar() {
        println "==================================================="
        println "[Task 2] Coletar Histórico de Versões (jan/2016+)"
        println "==================================================="

        Document doc = Jsoup.connect(URL_HISTORICO)
                .userAgent("Mozilla/5.0 (compatible; DesafioCrawler/1.0)")
                .timeout(30_000)
                .get()

        def linhas = doc.select("table tbody tr")
        assert !linhas.isEmpty() : "Tabela de histórico vazia"

        def registros = []
        linhas.each { tr ->
            def celulas = tr.select("td")
            if (celulas.size() < 3) return

            String competencia    = celulas[0].text().trim()
            String publicacao     = celulas[1].text().trim()
            String inicioVigencia = celulas[2].text().trim()

            Integer ano = extrairAno(competencia)
            if (ano != null && ano >= 2016) {
                registros << [competencia, publicacao, inicioVigencia]
            }
        }

        salvarCsv(registros)
        println "[OK] ${registros.size()} registros salvos em:"
        println "     ${ARQUIVO_SAIDA.absolutePath}"
        println "==================================================="
    }

    static Integer extrairAno(String competencia) {
        if (competencia == null || !competencia.contains("/")) return null
        try {
            return competencia.split("/")[1].trim().toInteger()
        } catch (NumberFormatException e) {
            return null
        }
    }

    static void salvarCsv(List<List<String>> registros) {
        ARQUIVO_SAIDA.parentFile.mkdirs()
        def sb = new StringBuilder()
        sb.append("Competência,Publicação,Início de Vigência\n")
        registros.each { r ->
            sb.append("${r[0]},${r[1]},${r[2]}\n")
        }
        ARQUIVO_SAIDA.setText(sb.toString(), "UTF-8")
    }
}