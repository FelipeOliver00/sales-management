package com.gvendas.gestaovendas.repositorio;

import com.gvendas.gestaovendas.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

    Cliente findByNome(String nome);
}
