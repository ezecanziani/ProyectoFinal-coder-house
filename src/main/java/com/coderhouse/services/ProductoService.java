package com.coderhouse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderhouse.models.Producto;
import com.coderhouse.repositories.ProductoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    public void deleteById(Long id) {
        productoRepository.deleteById(id);
    }

    public boolean isStockAvailable(Long productId, int cantidad) {
        Optional<Producto> producto = productoRepository.findById(productId);
        return producto.map(p -> p.getStock() >= cantidad).orElse(false);
    }
}
