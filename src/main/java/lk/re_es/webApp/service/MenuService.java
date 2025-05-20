package lk.re_es.webApp.service;

import lk.re_es.webApp.model.Menu;
import lk.re_es.webApp.dto.MenuResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class MenuService {

    private final File file = new File("database/menus.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Menu> getAllMenus() {
        return readMenusFromFile();
    }

    public Menu getMenuById(Long id) {
        return readMenusFromFile().stream()
                .filter(menu -> menu.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Menu addMenu(MenuResponse dto) {
        List<Menu> menus = readMenusFromFile();
        Long newId = menus.stream().mapToLong(Menu::getId).max().orElse(0) + 1;
        Menu newMenu = new Menu(newId, dto.getName(), dto.getDescription(), dto.getPrice());
        menus.add(newMenu);
        writeMenusToFile(menus);
        return newMenu;
    }

    public Menu updateMenu(Long id, MenuResponse dto) {
        List<Menu> menus = readMenusFromFile();
        for (Menu menu : menus) {
            if (menu.getId().equals(id)) {
                menu.setName(dto.getName());
                menu.setDescription(dto.getDescription());
                menu.setPrice(dto.getPrice());
                writeMenusToFile(menus);
                return menu;
            }
        }
        return null;
    }

    private List<Menu> readMenusFromFile() {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                writeMenusToFile(new ArrayList<>());
            }
            return mapper.readValue(file, new TypeReference<List<Menu>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to read menu file", e);
        }
    }

    private void writeMenusToFile(List<Menu> menus) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, menus);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write menu file", e);
        }
    }
}