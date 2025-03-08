package br.insper.loja.compra;

import br.insper.loja.evento.EventoService;
import br.insper.loja.produto.ProdutoService;
import br.insper.loja.usuario.Usuario;
import br.insper.loja.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private ProdutoService produtoService;

    // Método para obter todas as compras
    public List<Compra> getCompras() {
        return compraRepository.findAll();
    }

    // Método para salvar uma compra
    public Compra salvarCompra(Compra compra) {
        // Verifica se o usuário existe
        Usuario usuario = usuarioService.getUsuario(compra.getUsuario());
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        // Valida se todos os produtos existem
        for (String nomeProduto : compra.getProdutos()) {
            // Verifica se o produto está disponível
            produtoService.getProduto(nomeProduto);
        }

        // Define a data da compra
        compra.setDataCompra(LocalDateTime.now());

        // Salva a compra no banco de dados
        Compra compraSalva = compraRepository.save(compra);

        // Registra um evento relacionado à compra (exemplo de integração com EventoService)
        eventoService.salvarEvento(compra.getUsuario(), "Compra realizada: " + compra.getNome());

        // Retorna a compra salva
        return compraSalva;
    }
}
