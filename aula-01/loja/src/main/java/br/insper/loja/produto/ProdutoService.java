package br.insper.loja.produto;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProdutoService {

    public void salvarProduto(Produto produto) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity("http://produto:8080/api/produto", produto, Produto.class);
    }

    public Produto getProduto(String nome) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://produto:8080/api/produto/" + nome, Produto.class);
    }
}

