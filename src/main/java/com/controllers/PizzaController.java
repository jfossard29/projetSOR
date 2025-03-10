package com.controllers;

import com.dtos.PizzaDto;
import com.repositories.PizzaRepository;
import com.services.PizzaService;
import com.services.impl.PizzaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur gérant les pizzas.
 */
@RestController
@RequestMapping("/pizza")
public class PizzaController {
    @Autowired
    private PizzaService pizzaService;

    @Autowired
    private PizzaRepository pizzaRepository;

    public PizzaController(PizzaServiceImpl pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public List<PizzaDto> getAllPizzas() {
        return pizzaService.getAllPizzas();
    }

    @GetMapping("/{id}")
    public PizzaDto getPizza(@PathVariable Long id){
        return pizzaService.getPizzaById(id);
    }

    @PostMapping
    public PizzaDto savePizza(@RequestBody PizzaDto pizzaDto){
        return pizzaService.savePizza(pizzaDto);
    }

    @DeleteMapping("/{id}")
    public Boolean deletePizza(@PathVariable Long id){
        return pizzaService.deletePizza(id);
    }

    @PutMapping("/{id}")
    public PizzaDto updatePizza(@PathVariable Long id, @RequestBody PizzaDto pizzaDto) {
        return pizzaService.updatePizza(id, pizzaDto);
    }
}
