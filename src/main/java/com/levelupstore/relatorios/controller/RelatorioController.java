package com.levelupstore.relatorios.controller;

import com.levelupstore.relatorios.dto.RelatorioEstoqueDTO;
import com.levelupstore.relatorios.service.RelatorioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    // Endpoint: GET /v1/relatorios/estoque
    @GetMapping("/estoque")
    public ResponseEntity<RelatorioEstoqueDTO> getRelatorioEstoque() {
        RelatorioEstoqueDTO relatorio = relatorioService.gerarRelatorioEstoque();
        return ResponseEntity.ok(relatorio);
    }

    // Endpoint: GET /v1/relatorios/vendas/total
    @GetMapping("/vendas/total")
    public ResponseEntity<Map<String, Object>> getTotalVendas() {
        BigDecimal total = relatorioService.calcularTotalVendas();

        // Retornando um Map simples para não precisar criar outro DTO agora
        Map<String, Object> response = new HashMap<>();
        response.put("valorTotalVendido", total);
        response.put("mensagem", "Soma de todas as vendas registradas");

        return ResponseEntity.ok(response);
    }
}