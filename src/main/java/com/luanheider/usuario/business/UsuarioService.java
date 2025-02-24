package com.luanheider.usuario.business;

import com.luanheider.usuario.business.converter.UsuarioConverter;
import com.luanheider.usuario.business.dto.UsuarioDTO;
import com.luanheider.usuario.infrastructure.entity.Usuario;
import com.luanheider.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        // usuario = usuarioRepository.save(usuario);
        // return usuarioConverter.paraUsuarioDTO(usuario);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }
}
