class TissCrawlerMain {
    static void main(String[] args) {
        println "=== Crawler ANS/TISS iniciando ==="
        println "Autor: [Myke]"
        println "=== Ambiente OK ==="

        TissDownloader.baixarComponenteComunicacao()
        println ""
        HistoricoVersoes.coletar()
        println ""
        TabelaErros.baixar()
    }
}