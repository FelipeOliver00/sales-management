package com.gvendas.gestaovendas.servico;

import com.gvendas.gestaovendas.dto.venda.*;
import com.gvendas.gestaovendas.entidades.Cliente;
import com.gvendas.gestaovendas.entidades.ItemVenda;
import com.gvendas.gestaovendas.entidades.Produto;
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
    private ProdutoServico produtoServico;

    @Autowired
    public VendaServico(VendaRepositorio vendaRepositorio, ClienteServico clienteServico, ItemVendaRepositorio itemVendaRepositorio, ProdutoServico produtoServico) {
        this.vendaRepositorio = vendaRepositorio;
        this.clienteServico = clienteServico;
        this.itemVendaRepositorio = itemVendaRepositorio;
        this.produtoServico = produtoServico;
    }



    public ClienteVendaResponseDTO listarVendaPorCliente(Long codigocliente) {
        Cliente cliente =  validarClienteVendaExiste(codigocliente);
        List<VendaResponseDTO> vendaResponseDtoList =
        vendaRepositorio.findByClienteCodigo(codigocliente).stream()
                .map(venda -> criandoVendaResponseDTO(venda, itemVendaRepositorio.findByVendaPorCodigo(venda.getCodigo()))).collect(Collectors.toList());
        return new ClienteVendaResponseDTO(cliente.getNome(), vendaResponseDtoList);

    }

    public ClienteVendaResponseDTO listarVendaPorCodigo(Long codigoVenda) {
        Venda venda = validarVendaExiste(codigoVenda);
        List<ItemVenda> itensVendaLista = itemVendaRepositorio.findByVendaPorCodigo(venda.getCodigo());
        return new ClienteVendaResponseDTO(venda.getCliente().getNome(), Arrays.asList(criandoVendaResponseDTO(venda, itensVendaLista)));
    }

    public ClienteVendaResponseDTO salvar(Long codigoCliente, VendaRequestDTO vendaDto) {
        Cliente cliente =  validarClienteVendaExiste(codigoCliente);
        validarProdutoExiste(vendaDto.getItensVendaDto());
        Venda vendaSalva = salvarVenda(cliente, vendaDto);
        return new ClienteVendaResponseDTO(vendaSalva.getCliente().getNome(), Arrays.asList(criandoVendaResponseDTO(vendaSalva, itemVendaRepositorio.findByVendaPorCodigo(vendaSalva.getCodigo()))));
    }

    private Venda salvarVenda(Cliente cliente, VendaRequestDTO vendaDto) {
        Venda vendaSalva = vendaRepositorio.save(new Venda(vendaDto.getData(), cliente));
        vendaDto.getItensVendaDto().stream().map(itemVendaDto -> criandoItemVenda(itemVendaDto, vendaSalva))
        .forEach(itemVendaRepositorio::save );
        return vendaSalva;
    }

    private void validarProdutoExiste(List<ItemVendaRequestDTO> itensVendaDto) {
        itensVendaDto.forEach(item -> produtoServico.validarProdutoExiste(item.getCodigoProduto()));
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
            throw new RegraNegocioException(String.format("O Cliente de código %s informdo não existeno cadastro.", codigocliente));
        }
        return cliente.get();
    }

    private ItemVenda criandoItemVenda(ItemVendaRequestDTO itemVendaDto, Venda venda) {
        return new ItemVenda(itemVendaDto.getQuantidade(), itemVendaDto.getPrecoVendido(), new Produto(itemVendaDto.getCodigoProduto()), venda);
    }
}
