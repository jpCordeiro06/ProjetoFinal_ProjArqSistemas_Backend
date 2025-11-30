package com.levelupstore.usuarios.service;

import com.levelupstore.exception.BusinessRuleException;import com.levelupstore.usuarios.model.Usuario;
import com.levelupstore.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario salvarUsuario(Usuario usuario) {
        // Regra de Negócio: Login Único
        Optional<Usuario> existente = usuarioRepository.findByLogin(usuario.getLogin());
        if (existente.isPresent() && !existente.get().getId().equals(usuario.getId())) {
            throw new BusinessRuleException("Já existe um usuário com este login.");
        }
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }
}