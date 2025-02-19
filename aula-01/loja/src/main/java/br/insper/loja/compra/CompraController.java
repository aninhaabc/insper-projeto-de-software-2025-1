package br.insper.loja.compra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compra")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @GetMapping
    public ResponseEntity<List<Compra>> getCompras() {
        return ResponseEntity.ok(compraService.getCompras());
    }

    @PostMapping
    public ResponseEntity<?> salvarCompra(@RequestBody Compra compra) {
        try {
            Compra compraSalva = compraService.salvarCompra(compra);
            return ResponseEntity.ok(compraSalva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
