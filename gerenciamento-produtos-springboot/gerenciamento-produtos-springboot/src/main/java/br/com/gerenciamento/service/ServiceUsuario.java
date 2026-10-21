package br.com.gerenciamento.service;

import br.com.gerenciamento.exception.CriptoExistsException;
import br.com.gerenciamento.exception.EmailExistsException;
import br.com.gerenciamento.exception.ServiceExc;
import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.repository.UsuarioRepository;
import br.com.gerenciamento.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

@Service
public class ServiceUsuario {

    private final UsuarioRepository usuarioRepository;

    public ServiceUsuario(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void salvarUsuario(Usuario usuario) throws Exception {
        if (usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())) {
            throw new EmailExistsException("Este e-mail já está cadastrado.");
        }
        if (usuarioRepository.existsByUserIgnoreCase(usuario.getUser())) {
            throw new EmailExistsException("Este usuário já está cadastrado.");
        }
        try {
            usuario.setSenha(Util.md5(usuario.getSenha()));
        } catch (NoSuchAlgorithmException e) {
            throw new CriptoExistsException("Erro na criptografia da senha.");
        }
        usuarioRepository.save(usuario);
    }

    public Usuario loginUser(String user, String senha) throws ServiceExc {
        try {
            return usuarioRepository.findByUserAndSenha(user, Util.md5(senha)).orElse(null);
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceExc("Não foi possível processar a senha.");
        }
    }
}
