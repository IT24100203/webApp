package com.restaurant.tablemanagement.controller;

import com.restaurant.tablemanagement.model.Table;
import com.restaurant.tablemanagement.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/tables")
public class TableController {
    
    private final TableService tableService;
    
    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }
    
    @GetMapping
    public String getAllTables(Model model) {
        List<Table> tables = tableService.getAllTables();
        model.addAttribute("tables", tables);
        model.addAttribute("newTable", new Table());
        return "tables";
    }
    
    @GetMapping("/sorted")
    public String getTablesSortedByCapacity(Model model) {
        List<Table> sortedTables = tableService.getTablesSortedByCapacity();
        model.addAttribute("tables", sortedTables);
        model.addAttribute("newTable", new Table());
        model.addAttribute("sorted", true);
        return "tables";
    }
    
    @GetMapping("/{id}")
    public String getTableById(@PathVariable Long id, Model model) {
        tableService.getTableById(id).ifPresent(table -> model.addAttribute("table", table));
        return "table-details";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        tableService.getTableById(id).ifPresent(table -> model.addAttribute("table", table));
        return "edit-table";
    }
    
    @PostMapping
    public String createTable(@ModelAttribute("newTable") Table table, RedirectAttributes redirectAttributes) {
        tableService.createTable(table);
        redirectAttributes.addFlashAttribute("success", "Table created successfully!");
        return "redirect:/tables";
    }
    
    @PostMapping("/update/{id}")
    public String updateTable(@PathVariable Long id, @ModelAttribute Table table, RedirectAttributes redirectAttributes) {
        tableService.updateTable(id, table);
        redirectAttributes.addFlashAttribute("success", "Table updated successfully!");
        return "redirect:/tables";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteTable(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (tableService.deleteTable(id)) {
            redirectAttributes.addFlashAttribute("success", "Table deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Table not found!");
        }
        return "redirect:/tables";
    }
}
