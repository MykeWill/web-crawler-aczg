import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class TissDownloader {

    static final String URL_INDICE =
            "https://www.gov.br/ans/pt-br/assuntos/prestadores/" +
                    "padrao-para-troca-de-informacao-de-saude-suplementar-2013-tiss"

    static final File PASTA_DOWNLOAD = new File("../Downloads/Arquivos_padrao_TISS")

    static void baixarComponenteComunicacao() {
        println "==================================================="
        println "[Task 1] Baixar Componente de Comunicação (TISS)"
        println "==================================================="

        PASTA_DOWNLOAD.mkdirs()

        String urlVersao = descobrirUrlVersaoMaisRecente()
        println "[1/3] Versão mais recente encontrada:"
        println "      ${urlVersao}"

        String urlZip = descobrirUrlComponenteComunicacao(urlVersao)
        println "[2/3] Componente de Comunicação:"
        println "      ${urlZip}"

        File destino = baixarArquivo(urlZip)
        println "[3/3] Arquivo salvo em:"
        println "      ${destino.absolutePath}"
        println "==================================================="
    }

    static String descobrirUrlVersaoMaisRecente() {
        Document doc = Jsoup.connect(URL_INDICE)
                .userAgent("Mozilla/5.0 (compatible; DesafioCrawler/1.0)")
                .timeout(30_000)
                .get()

        def h2Versao = doc.select("h2").find { h2 ->
            String t = h2.text().trim()
            t.startsWith("Padrão TISS") &&
                    !t.contains("Histórico") &&
                    !t.contains("Tabelas")
        }

        assert h2Versao != null : "Não encontrei o <h2> da versão mais recente"

        def link = h2Versao.nextElementSibling()
                ?.selectFirst("a[href]")

        assert link != null : "Não encontrei o link da versão mais recente"
        return link.absUrl("href")
    }

    static String descobrirUrlComponenteComunicacao(String urlVersao) {
        Document doc = Jsoup.connect(urlVersao)
                .userAgent("Mozilla/5.0 (compatible; DesafioCrawler/1.0)")
                .timeout(30_000)
                .get()

        def linhas = doc.select("table tbody tr")
        assert !linhas.isEmpty() : "Tabela de arquivos vazia em ${urlVersao}"

        def linhaComunicacao = linhas.find { tr ->
            def primeiraCelula = tr.selectFirst("td")
            primeiraCelula != null &&
                    primeiraCelula.text().trim().equalsIgnoreCase("Componente de Comunicação")
        }

        assert linhaComunicacao != null :
                "Não encontrei a linha 'Componente de Comunicação' na tabela"

        def linkZip = linhaComunicacao.select("a[href]").find { a ->
            a.absUrl("href").toLowerCase().endsWith(".zip")
        }

        assert linkZip != null : "Não encontrei link .zip na linha do Componente de Comunicação"
        return linkZip.absUrl("href")
    }

    static File baixarArquivo(String url) {
        String nomeArquivo = url.tokenize("/").last()
        File destino = new File(PASTA_DOWNLOAD, nomeArquivo)

        if (destino.exists() && destino.length() > 0) {
            println "      Arquivo já existe, pulando download: ${nomeArquivo}"
            return destino
        }

        println "      Baixando '${nomeArquivo}'..."

        def resposta = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (compatible; DesafioCrawler/1.0)")
                .timeout(60_000)
                .ignoreContentType(true)
                .execute()

        destino.bytes = resposta.bodyAsBytes()

        return destino
    }
}