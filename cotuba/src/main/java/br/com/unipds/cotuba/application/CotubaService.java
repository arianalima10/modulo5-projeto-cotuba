package br.com.unipds.cotuba.application;

import br.com.unipds.cotuba.domain.Capitulo;
import br.com.unipds.cotuba.domain.EbookBuilder;
import br.com.unipds.cotuba.domain.Markdown;
import br.com.unipds.cotuba.dto.ParametrosCotuba;
import br.com.unipds.cotuba.ports.in.CotubaUseCase;
import br.com.unipds.cotuba.ports.out.GeradorEbook;
import br.com.unipds.cotuba.ports.out.LeitorPropriedadesEbook;
import br.com.unipds.cotuba.ports.out.RenderizadorMarkdown;
import br.com.unipds.cotuba.ports.out.RepositorioMarkDowns;
import br.com.unipds.cotuba.support.FormatoEbookFilter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.jmolecules.ddd.annotation.Service;

import java.nio.file.Path;
import java.util.List;

@Service
@ApplicationScoped
public class CotubaService implements CotubaUseCase { // Essa classe será controlada pelo CDI

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

    @Override
    public void executar(ParametrosCotuba parametrosCotuba) {

        Path diretorioMDs = parametrosCotuba.diretorioDosMD();

        List<Markdown> markdowns = repositorioMarkdowns.buscarArquivosMD(diretorioMDs);

        List<Capitulo> capitulos = renderizadorMarkdown.renderizar(markdowns);

        var propriedadesEbook = leitorPropriedadesEbook.ler(diretorioMDs);

        var ebook = EbookBuilder.builder()
                .capitulos(capitulos)
                .formato(parametrosCotuba.formato())
                .titulo(propriedadesEbook.titulo())
                .autor(propriedadesEbook.autor()).build();

        GeradorEbook geradorEbook = geradororesEbook.select(FormatoEbookFilter.of(ebook.formato())).get();

        geradorEbook.gerar(ebook, parametrosCotuba.arquivoDeSaida());
    }
}