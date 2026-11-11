package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import java.nio.file.Path;
import java.util.List;

@ApplicationScoped
public class CotubaService { // Essa classe será controlada pelo CDI

    private final RenderizadorMarkdown renderizadorMarkdown;
    private final LeitorPropriedadesEbook leitorPropriedadesEbook;
    private final RepositorioMarkDowns repositorioMarkdowns;
    private final Instance<GeradorEbook> geradororesEbook;

    @Inject
    public CotubaService(RenderizadorMarkdown renderizadorMarkdown, LeitorPropriedadesEbook leitorPropriedadesEbook,
                         RepositorioMarkDowns repositorioMarkdowns, @Any Instance<GeradorEbook> geradororesEbook) {
        this.renderizadorMarkdown = renderizadorMarkdown;
        this.leitorPropriedadesEbook = leitorPropriedadesEbook;
        this.repositorioMarkdowns = repositorioMarkdowns;
        this.geradororesEbook = geradororesEbook;
    }

    public void executar(ParametrosCotuba parametrosCotuba) {

        Path diretorioMDs = parametrosCotuba.diretorioDosMD();

        List<Markdown> markdowns = repositorioMarkdowns.buscarArquivosMD(diretorioMDs);

        List<Capitulo> capitulos = renderizadorMarkdown.renderizar(markdowns);

        var propriedadesEbook = leitorPropriedadesEbook.ler(diretorioMDs);

        var ebook = EbookBuilder.builder()
                .capitulos(capitulos)
                .formato(parametrosCotuba.formato())
                .arquivoSaida(parametrosCotuba.arquivoDeSaida())
                .titulo(propriedadesEbook.titulo())
                .autor(propriedadesEbook.autor()).build();

        GeradorEbook geradorEbook = geradororesEbook.select(FormatoEbookFilter.of(ebook.formato())).get();

        geradorEbook.gerar(ebook);
    }
}