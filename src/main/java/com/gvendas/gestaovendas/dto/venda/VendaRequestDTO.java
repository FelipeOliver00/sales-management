package com.gvendas.gestaovendas.dto.venda;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@ApiModel("Venda requisição DTO")
public class VendaRequestDTO {

    @ApiModelProperty(value = "Data")
    @NotNull(message = "Data")
    private LocalDate data;
    @ApiModelProperty(value = "Itens da venda")
    @NotNull(message = "Items venda")
    @Valid
    private List<ItemVendaRequestDTO> itensVendaDto;

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<ItemVendaRequestDTO> getItensVendaDto() {
        return itensVendaDto;
    }

    public void setItensVendaDto(List<ItemVendaRequestDTO> itensVendaDto) {
        this.itensVendaDto = itensVendaDto;
    }
}
