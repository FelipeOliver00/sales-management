package com.gvendas.gestaovendas.repositorio;

import com.gvendas.gestaovendas.entidades.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {
    // from categoria dog where dog.nome = valor do parametro
    Categoria findByNome(String nome);
}
