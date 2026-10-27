package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.inject.Named;

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

        Path diretorioMDs = parametrosCotuba.getDiretorioDosMD();

        List<Capitulo> capitulos = repositorioMarkdowns.buscarArquivosMD(diretorioMDs);

        renderizadorMarkdown.renderizar(capitulos);

        var ebook = new Ebook();

        leitorPropriedadesEbook.ler(parametrosCotuba.getDiretorioDosMD(), ebook);

        ebook.setCapitulos(capitulos);
        ebook.setFormato(parametrosCotuba.getFormato());
        ebook.setArquivoSaida(parametrosCotuba.getArquivoDeSaida());

        FormatoEbook formato = ebook.getFormato();
        GeradorEbook geradorEbook = geradororesEbook.select(FormatoEbookFilter.of(formato)).get();

        geradorEbook.gerar(ebook);
    }
}