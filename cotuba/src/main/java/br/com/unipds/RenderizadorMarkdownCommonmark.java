package br.com.unipds;

import jakarta.enterprise.context.ApplicationScoped;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.List;

@ApplicationScoped
public class RenderizadorMarkdownCommonmark implements RenderizadorMarkdown {

    @Override
    public void renderizar(List<Capitulo> capitulos) {

            capitulos.forEach(capitulo -> {

                Parser parser = Parser.builder().build();
                Node document = null;
                try {
                    String markdown = capitulo.getMarkdown();

                    document = parser.parse(markdown);

                    document.accept(new AbstractVisitor() {
                        @Override
                        public void visit(Heading heading) {
                            if (heading.getLevel() == 1) {
                                // capítulo
                                String tituloDoCapitulo = ((Text) heading.getFirstChild()).getLiteral();
                                capitulo.setTitulo(tituloDoCapitulo);
                            } else if (heading.getLevel() == 2) {
                                // seção
                            } else if (heading.getLevel() == 3) {
                                // título
                            }

                            // Permite percorrer os nós filhos do título, possibilitando o processamento de outros
                            // elementos do Markdown.
                            visitChildren(heading);
                        }
                    });
                } catch (Exception ex) {
                    throw new IllegalStateException("Erro ao fazer parse do arquivo "
                            + capitulo.getArquivoMarkdown(), ex);
                }

                try {
                    HtmlRenderer renderer = HtmlRenderer.builder().build();
                    String html = renderer.render(document);
                    capitulo.setHtml(html);
                } catch (Exception ex) {
                    throw new IllegalStateException("Erro ao renderizar para HTML o arquivo "
                            + capitulo.getArquivoMarkdown(), ex);
                }
            });
    }
}
