package com.levelupstore.usuarios.service;

import com.levelupstore.exception.BusinessRuleException;
import com.levelupstore.usuarios.model.Cliente;
import com.levelupstore.usuarios.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente salvarCliente(Cliente cliente) {
        // Regra de Negócio: CPF Único
        if (cliente.getCpf() != null) {
            Optional<Cliente> existente = clienteRepository.findByCpf(cliente.getCpf());
            if (existente.isPresent() && !existente.get().getId().equals(cliente.getId())) {
                throw new BusinessRuleException("Já existe um cliente com este CPF.");
            }
        }
        return clienteRepository.save(cliente);
    }

    public List<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }
}