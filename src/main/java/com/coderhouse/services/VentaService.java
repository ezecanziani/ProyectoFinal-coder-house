package com.coderhouse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.coderhouse.DTOs.LineaVentaDTO;
import com.coderhouse.DTOs.VentaDTO;
import com.coderhouse.excepciones.InsufficientStockException;
import com.coderhouse.models.Cliente;
import com.coderhouse.models.Producto;
import com.coderhouse.models.Venta;


import ch.qos.logback.core.net.server.Client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class VentaService<VentaRepository> {
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProductoService productoService;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public Venta createVenta(VentaDTO ventaDTO) {
    	Client cliente = clienteService.findById(ventaDTO.getClienteId())
    	        .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        List<Producto> productos = new ArrayList<>();
        double total = 0;

        for (LineaVentaDTO linea : ventaDTO.getLineas()) {
            Producto producto = productoService.findById(linea.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            if (!productoService.isStockAvailable(producto.getId(), linea.getCantidad())) {
                throw new InsufficientStockException("Stock insuficiente para el producto: " + producto.getNombre());
            }
            producto.setStock(producto.getStock() - linea.getCantidad());
            productoService.save(producto);

            productos.add(producto);
            total += producto.getPrecio() * linea.getCantidad();
        }

        LocalDateTime fecha = obtenerFechaDesdeAPI();

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setProductos(productos);
        venta.setFecha(fecha);
        venta.setTotal(total);

        return ventaRepository.save(venta);
    }

    private LocalDateTime obtenerFechaDesdeAPI() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity("http://worldclockapi.com/api/json/utc/now", Map.class);
            String currentDateTime = (String) response.getBody().get("currentDateTime");
            return LocalDateTime.parse(currentDateTime, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}
