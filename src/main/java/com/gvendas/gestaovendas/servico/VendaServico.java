package com.gvendas.gestaovendas.servico;

import com.gvendas.gestaovendas.dto.venda.ClienteVendaResponseDTO;
import com.gvendas.gestaovendas.dto.venda.ItemVendaResponseDTO;
import com.gvendas.gestaovendas.dto.venda.VendaResponseDTO;
import com.gvendas.gestaovendas.entidades.Cliente;
import com.gvendas.gestaovendas.entidades.ItemVenda;
import com.gvendas.gestaovendas.entidades.Venda;
import com.gvendas.gestaovendas.excecao.RegraNegocioException;
import com.gvendas.gestaovendas.repositorio.ItemVendaRepositorio;
import com.gvendas.gestaovendas.repositorio.VendaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendaServico extends AbstractVendaServico{

    private VendaRepositorio vendaRepositorio;
    private ItemVendaRepositorio itemVendaRepositorio;
    private ClienteServico clienteServico;

    @Autowired
    public VendaServico(VendaRepositorio vendaRepositorio, ClienteServico clienteServico, ItemVendaRepositorio itemVendaRepositorio) {
        this.vendaRepositorio = vendaRepositorio;
        this.clienteServico = clienteServico;
        this.itemVendaRepositorio = itemVendaRepositorio;
    }



    public ClienteVendaResponseDTO listarVendaPorCliente(Long codigocliente) {
        Cliente cliente =  validarClienteVendaExiste(codigocliente);
        List<VendaResponseDTO> vendaResponseDtoList =
        vendaRepositorio.findByClienteCodigo(codigocliente).stream()
                .map(venda -> criandoVendaResponseDTO(venda, itemVendaRepositorio.findByVendaCodigo(venda.getCodigo()))).collect(Collectors.toList());
        return new ClienteVendaResponseDTO(cliente.getNome(), vendaResponseDtoList);

    }

    public ClienteVendaResponseDTO listarVendaPorCodigo(Long codigoVenda) {
        Venda venda = validarVendaExiste(codigoVenda);
        List<ItemVenda> itensVendaLista = itemVendaRepositorio.findByVendaCodigo(venda.getCodigo());
        return new ClienteVendaResponseDTO(venda.getCliente().getNome(), Arrays.asList(criandoVendaResponseDTO(venda, itensVendaLista)));
    }

    private Venda validarVendaExiste(Long codigoVenda) {
        Optional<Venda> venda = vendaRepositorio.findById(codigoVenda);
        if (venda.isEmpty()) {
            throw new RegraNegocioException(String.format("Venda de código %s não encontrada", codigoVenda));
        }
        return venda.get();
    }

    private Cliente validarClienteVendaExiste(Long codigocliente) {
        Optional<Cliente> cliente =  clienteServico.buscarPorCodigo(codigocliente);
        if(cliente.isEmpty()) {
            throw new RegraNegocioException(String.format("O Cliente de código % informdo não existeno cadastro.", codigocliente));
        }
        return cliente.get();
    }
}
