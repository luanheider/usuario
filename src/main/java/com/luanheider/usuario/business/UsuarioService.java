package com.luanheider.usuario.business;

import com.luanheider.usuario.business.converter.UsuarioConverter;
import com.luanheider.usuario.business.dto.EnderecoDTO;
import com.luanheider.usuario.business.dto.TelefoneDTO;
import com.luanheider.usuario.business.dto.UsuarioDTO;
import com.luanheider.usuario.infrastructure.entity.Endereco;
import com.luanheider.usuario.infrastructure.entity.Telefone;
import com.luanheider.usuario.infrastructure.entity.Usuario;
import com.luanheider.usuario.infrastructure.exceptions.ConflictException;
import com.luanheider.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.luanheider.usuario.infrastructure.repository.EnderecoRepository;
import com.luanheider.usuario.infrastructure.repository.TelefoneRepository;
import com.luanheider.usuario.infrastructure.repository.UsuarioRepository;
import com.luanheider.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("Email já cadastrado" + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado", e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email).orElseThrow(
                    () -> new ResourceNotFoundException("Email não encontrado " + email)));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email não encontrado ", e.getCause());
        }
    }

    public void deleteUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO usuarioDTO) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null);
        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Email não encontrado."));
        Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO, usuarioEntity);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaEndereco(Long id, EnderecoDTO enderecoDTO) {
        Endereco entity = enderecoRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("ID não encontrado " + id));
        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long id, TelefoneDTO telefoneDTO) {
        Telefone entity = telefoneRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("ID não encontrado " + id));
        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, entity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }
}
