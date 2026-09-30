import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class TabelaErros {

    static final String URL_TABELAS =
            "https://www.gov.br/ans/pt-br/assuntos/prestadores/" +
                    "padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss/" +
                    "padrao-tiss-tabelas-relacionadas"

    static void baixar() {
        println "==================================================="
        println "[Task 3] Baixar Tabela de erros no envio para a ANS"
        println "==================================================="

        String urlArquivo = descobrirUrlTabelaErros()
        println "[1/2] Tabela encontrada:"
        println "      ${urlArquivo}"

        File destino = TissDownloader.baixarArquivo(urlArquivo)
        println "[2/2] Arquivo salvo em:"
        println "      ${destino.absolutePath}"
        println "==================================================="
    }

    static String descobrirUrlTabelaErros() {
        Document doc = Jsoup.connect(URL_TABELAS)
                .userAgent("Mozilla/5.0 (compatible; DesafioCrawler/1.0)")
                .timeout(30_000)
                .get()

        def h2Alvo = doc.select("h2").find { h2 ->
            h2.text().toLowerCase().contains("tabela de erros")
        }

        assert h2Alvo != null : "Não encontrei o <h2> da Tabela de erros"

        def link = h2Alvo.nextElementSibling()
                ?.selectFirst("a[href]")

        assert link != null : "Não encontrei o link da Tabela de erros"
        return link.absUrl("href")
    }
}