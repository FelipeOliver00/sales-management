package com.gvendas.gestaovendas.controlador;

import com.gvendas.gestaovendas.dto.cliente.ClienteRequestDTO;
import com.gvendas.gestaovendas.dto.cliente.ClienteResponseDTO;
import com.gvendas.gestaovendas.entidades.Cliente;
import com.gvendas.gestaovendas.servico.ClienteServico;
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

@Api(tags = "Cliente")
@RestController
@RequestMapping("cliente")
public class ClienteControlador {

    @Autowired
    private ClienteServico clienteServico;

    @ApiOperation(value = "Listar", nickname = "listarTodasCliente")
    @GetMapping
    public List<ClienteResponseDTO> listarTodas() {
        return clienteServico.listarTodas().stream().map(cliente ->
                ClienteResponseDTO.converterParaClienteDTO(cliente)).collect(Collectors.toList());
    }

    @ApiOperation(value = "Listar por código", nickname = "buscarClientePorId")
    @GetMapping("/{codigo}")
    public ResponseEntity<ClienteResponseDTO> buscarPorCodigo(@PathVariable Long codigo) {
        Optional<Cliente> cliente = clienteServico.buscarPorCodigo(codigo);
        return cliente.isPresent() ? ResponseEntity.ok(ClienteResponseDTO.converterParaClienteDTO(cliente.get()))
                : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Salvar", nickname = "salvarCliente")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> salvar(@Valid @RequestBody ClienteRequestDTO clienteDto){
        Cliente clienteSalvo = clienteServico.salvar(clienteDto.conveterParaEntidade());
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteResponseDTO.converterParaClienteDTO(clienteSalvo));
    }

    @ApiOperation(value = "Atualizar", nickname = "atualizarCliente")
    @PutMapping("/{codigo}")
    public ResponseEntity<ClienteResponseDTO> atualizar(@Valid @PathVariable Long codigo, @RequestBody ClienteRequestDTO clienteDto) {
        Cliente clienteAtualizado = clienteServico.atualizar(codigo, clienteDto.conveterParaEntidade(codigo));
        return ResponseEntity.ok(ClienteResponseDTO.converterParaClienteDTO(clienteAtualizado));
    }

    @ApiOperation(value = "Deletar", nickname = "deletarCliente")
    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long codigo) {
        clienteServico.deletar(codigo);
    }

}
