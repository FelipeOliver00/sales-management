package com.gvendas.gestaovendas.controlador;

import com.gvendas.gestaovendas.dto.categoria.CategoriaRequestDTO;
import com.gvendas.gestaovendas.dto.categoria.CategoriaResponseDTO;
import com.gvendas.gestaovendas.entidades.Categoria;
import com.gvendas.gestaovendas.servico.CategoriaServico;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(tags = "Categoria")
@RestController
@RequestMapping("/categoria")
public class CategoriaControlador {

    @Autowired
    private CategoriaServico categoriaServico;

    @ApiOperation(value = "Listar", nickname = "listarTodas")
    @GetMapping
    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaServico.listarTodas().stream().map(categoria ->
                        CategoriaResponseDTO.converterParaCategoriaDTO(categoria))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Listar por código", nickname = "buscarPorCodigo")
    @GetMapping("/{codigo}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorCodigo(@PathVariable Long codigo) {
        Optional<Categoria> categoria = categoriaServico.buscarPorCodigo(codigo);
        return categoria.isPresent() ? ResponseEntity.ok(CategoriaResponseDTO.converterParaCategoriaDTO(categoria.get())) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Salvar", nickname = "salvarCategoria")
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> salvar(@Valid @RequestBody CategoriaRequestDTO categoriaDto) {
        Categoria categoriaSalvar = categoriaServico.salvar(categoriaDto.conveterParaEntidade());
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoriaResponseDTO.converterParaCategoriaDTO(categoriaSalvar));
    }

    @ApiOperation(value = "Atualizar", nickname = "atualizarCategoria")
    @PutMapping("/{codigo}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long codigo, @RequestBody CategoriaRequestDTO categoriaDto) {
        Categoria categoriaAtualizada = categoriaServico.atualizar(codigo, categoriaDto.conveterParaEntidade(codigo));
        return ResponseEntity.ok(CategoriaResponseDTO.converterParaCategoriaDTO(categoriaAtualizada));
    }

    @ApiOperation(value = "Deletar", nickname = "deletarCategoria")
    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long codigo) {
        categoriaServico.deletar(codigo);
    }

}
