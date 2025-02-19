package br.insper.loja.compra;

import br.insper.loja.evento.EventoService;
import br.insper.loja.produto.Produto;
import br.insper.loja.produto.ProdutoRepository;
import br.insper.loja.usuario.Usuario;
import br.insper.loja.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private ProdutoRepository produtoRepository;

    public Compra salvarCompra(Compra compra) {
        // Verifica se o usuário existe
        Usuario usuario = usuarioService.getUsuario(compra.getUsuario());
        compra.setNome(usuario.getNome());
        compra.setDataCompra(LocalDateTime.now());

        double totalCompra = 0.0;

        // Percorre os produtos comprados
        for (String produtoId : compra.getProdutos()) {
            Optional<Produto> produtoOpt = produtoRepository.findById(produtoId);

            if (produtoOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto com ID " + produtoId + " não encontrado.");
            }

            Produto produto = produtoOpt.get();

            // Verifica se há estoque suficiente
            if (produto.getQuantidadeEmEstoque() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto " + produto.getNome() + " está sem estoque.");
            }

            // Atualiza estoque e calcula o preço total
            produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() - 1);
            totalCompra += produto.getPreco();
            produtoRepository.save(produto);
        }

        eventoService.salvarEvento(usuario.getEmail(), "Compra realizada - Total: R$" + totalCompra);

        return compraRepository.save(compra);
    }

    public List<Compra> getCompras() {
        return compraRepository.findAll();
    }
}
