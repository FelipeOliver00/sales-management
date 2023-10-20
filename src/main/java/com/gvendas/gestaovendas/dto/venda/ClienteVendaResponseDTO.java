package com.gvendas.gestaovendas.dto.venda;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("Cliente da venda retorno DTO")
public class ClienteVendaResponseDTO {

    @ApiModelProperty(value = "Nome cliente")
    private String nome;

    @ApiModelProperty(value = "Venda")
    private List<VendaResponseDTO> vendaResponseDTOS;

    public ClienteVendaResponseDTO(String nome, List<VendaResponseDTO> vendaResponseDTOS) {
        this.nome = nome;
        this.vendaResponseDTOS = vendaResponseDTOS;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<VendaResponseDTO> getVendaResponseDTOS() {
        return vendaResponseDTOS;
    }

    public void setVendaResponseDTOS(List<VendaResponseDTO> vendaResponseDTOS) {
        this.vendaResponseDTOS = vendaResponseDTOS;
    }
}
