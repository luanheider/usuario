package com.lmh.usuario.infrasctructure.business.converter;

import com.lmh.usuario.infrasctructure.business.dto.EnderecoDTO;
import com.lmh.usuario.infrasctructure.business.dto.TelefoneDTO;
import com.lmh.usuario.infrasctructure.business.dto.UsuarioDTO;
import com.lmh.usuario.infrasctructure.entity.Endereco;
import com.lmh.usuario.infrasctructure.entity.Telefone;
import com.lmh.usuario.infrasctructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioConverter {
    public Usuario paraUsuario(UsuarioDTO usuarioDTO) {
        return Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .enderecos(paraListEndereco(usuarioDTO.getEnderecos()))
                .telefones(paraListTelefone(usuarioDTO.getTelefones()))
                .build();
    }

    public List<Endereco> paraListEndereco(List<EnderecoDTO> enderecoDTO) {
        return enderecoDTO.stream().map(this::paraEndereco).toList();
    }

    public Endereco paraEndereco(EnderecoDTO enderecoDTO) {
        return Endereco.builder().cep(enderecoDTO.getCep()).rua(enderecoDTO.getRua()).cidade(enderecoDTO.getCidade()).estado(enderecoDTO.getEstado()).complemento(enderecoDTO.getComplemento()).build();
    }

    public List<Telefone> paraListTelefone(List<TelefoneDTO> telefoneDTO) {
        return telefoneDTO.stream().map(this::paraTelefone).toList();
    }

    public Telefone paraTelefone(TelefoneDTO telefoneDTO) {
        return Telefone.builder().numero(telefoneDTO.getNumero()).ddd(telefoneDTO.getDdd()).build();
    }

    public UsuarioDTO paraUsuarioDTO(Usuario usuario) {
        return UsuarioDTO.builder().nome(usuario.getNome()).email(usuario.getEmail()).senha(usuario.getSenha()).enderecos(paraListEnderecoDTO(usuario.getEnderecos())).telefones(paraListTelefoneDTO(usuario.getTelefones())).build();
    }

    public List<EnderecoDTO> paraListEnderecoDTO(List<Endereco> enderecos) {
        return enderecos.stream().map(this::paraEnderecoDTO).toList();
    }

    public EnderecoDTO paraEnderecoDTO(Endereco endereco) {
        return EnderecoDTO.builder()
                .cep(endereco.getCep())
                .rua(endereco.getRua())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .complemento(endereco.getComplemento())
                .build();
    }

    public List<TelefoneDTO> paraListTelefoneDTO(List<Telefone> telefones) {
        return telefones.stream().map(this::paraTelefoneDTO).toList();
    }

    public TelefoneDTO paraTelefoneDTO(Telefone telefone) {
        return TelefoneDTO.builder().numero(telefone.getNumero()).ddd(telefone.getDdd()).build();
    }

    public Usuario updateUsuario(UsuarioDTO usuarioDTO, Usuario usuarioEntity) {
        return Usuario.builder()
                .nome(usuarioDTO.getNome() != null ? usuarioDTO.getNome() : usuarioEntity.getNome())
                .email(usuarioDTO.getEmail() != null ? usuarioDTO.getEmail() : usuarioEntity.getEmail())
                .senha(usuarioDTO.getSenha() != null ? usuarioDTO.getSenha() : usuarioEntity.getSenha())
                .enderecos(usuarioEntity.getEnderecos())
                .telefones(usuarioEntity.getTelefones())
                .id(usuarioEntity.getId())
                .build();
    }
}
