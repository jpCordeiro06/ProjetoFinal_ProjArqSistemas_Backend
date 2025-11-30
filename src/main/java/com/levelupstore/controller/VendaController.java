package com.levelupstore.controller;

import com.levelupstore.pdv.model.Venda;
import com.levelupstore.pdv.service.VendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/vendas")
public class VendaController {

    private final VendaService vendaService;

    // Injeção de dependência via construtor
    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    public ResponseEntity<Object> realizarVenda(@RequestBody Venda venda) {
        try {
            // O Service cuida de tudo: validação, baixa de estoque e salvamento
            Venda novaVenda = vendaService.realizarVenda(venda);
            return new ResponseEntity<>(novaVenda, HttpStatus.CREATED);

        } catch (IllegalArgumentException | IllegalStateException e) {
            // Retorna erro 400 se alguma validação falhar
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Retorna erro 500 para falhas inesperadas
            return new ResponseEntity<>("Erro ao processar venda: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}