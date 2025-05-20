package lk.re_es.webApp.controller;

import lk.re_es.webApp.dto.MenuResponse;
import lk.re_es.webApp.model.Menu;
import lk.re_es.webApp.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@CrossOrigin(origins = "*") // This allows requests from any origin
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping
    public List<Menu> getAllMenus() {
        return menuService.getAllMenus();
    }

    @GetMapping("/{id}")
    public Menu getMenuById(@PathVariable Long id) {
        return menuService.getMenuById(id);
    }

    @PostMapping
    public Menu addMenu(@RequestBody MenuResponse menuDTO) {
        return menuService.addMenu(menuDTO);
    }

    @PutMapping("/{id}")
    public Menu updateMenu(@PathVariable Long id, @RequestBody MenuResponse menuDTO) {
        return menuService.updateMenu(id, menuDTO);
    }
}