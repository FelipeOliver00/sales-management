package com.gvendas.gestaovendas.repositorio;

import com.gvendas.gestaovendas.entidades.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendaRepositorio extends JpaRepository<Venda, Long> {

    List<Venda> findByClienteCodigo(Long codigoCliente);
}
