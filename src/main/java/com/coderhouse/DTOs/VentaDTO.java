package com.coderhouse.DTOs;

import java.util.List;

public class VentaDTO {
    private Long clienteId;
    private List<LineaVentaDTO> lineas;


    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<LineaVentaDTO> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaVentaDTO> lineas) {
        this.lineas = lineas;
    }
}