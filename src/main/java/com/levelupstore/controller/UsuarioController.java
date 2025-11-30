package com.levelupstore.controller;

import com.levelupstore.usuarios.model.Cliente;
import com.levelupstore.usuarios.model.Usuario;
import com.levelupstore.usuarios.service.ClienteService;
import com.levelupstore.usuarios.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ClienteService clienteService;

    public UsuarioController(UsuarioService usuarioService, ClienteService clienteService) {
        this.usuarioService = usuarioService;
        this.clienteService = clienteService;
    }

    // Endpoint para criar Vendedor/Admin
    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
        return new ResponseEntity<>(usuarioService.salvarUsuario(usuario), HttpStatus.CREATED);
    }

    // Endpoint para criar Cliente (vamos deixar aqui por praticidade)
    @PostMapping("/clientes")
    public ResponseEntity<Cliente> criarCliente(@RequestBody Cliente cliente) {
        return new ResponseEntity<>(clienteService.salvarCliente(cliente), HttpStatus.CREATED);
    }
}