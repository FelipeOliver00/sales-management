package com.gvendas.gestaovendas.servico;

import com.gvendas.gestaovendas.dto.venda.ClienteVendaResponseDTO;
import com.gvendas.gestaovendas.dto.venda.ItemVendaResponseDTO;
import com.gvendas.gestaovendas.dto.venda.VendaResponseDTO;
import com.gvendas.gestaovendas.entidades.ItemVenda;
import com.gvendas.gestaovendas.entidades.Venda;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractVendaServico {

    protected ClienteVendaResponseDTO retornandoClienteVendaResponseDTO(Venda venda, List<ItemVenda> itensVendaList){
        return new ClienteVendaResponseDTO(venda.getCliente().getNome(), Arrays.asList(
                criandoVendaResponseDTO(venda, itensVendaList)));
    }
    protected VendaResponseDTO criandoVendaResponseDTO(Venda venda, List<ItemVenda> itensVendaList) {
        List<ItemVendaResponseDTO> itensVendaResponseDto = itensVendaList
                .stream().map(this::criandoItensVendaResponseDto).collect(Collectors.toList());
        return new VendaResponseDTO(venda.getCodigo(), venda.getData(), itensVendaResponseDto);
    }

    protected ItemVendaResponseDTO criandoItensVendaResponseDto(ItemVenda itemVenda) {
        return new ItemVendaResponseDTO(itemVenda.getCodigo(), itemVenda.getQuantidade(), itemVenda.getPrecoVendido(),
                itemVenda.getProduto().getCodigo(), itemVenda.getProduto().getDescricao());
    }
}
