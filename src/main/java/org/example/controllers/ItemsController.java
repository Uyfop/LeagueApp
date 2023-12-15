package org.example.controllers;
import org.example.services.ItemService;
import org.example.tables.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ItemsController {

    private final ItemService itemService;

    public ItemsController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(value = "/item/{itemName}")
    public ResponseEntity<Items> getItemByName(@PathVariable String itemName) {
        Items item = itemService.GetItemByName(itemName);
        return ResponseEntity.ok(item);
    }

    @GetMapping(value = "/item/all")
    public ResponseEntity<List<Items>> getAllItems() {
        List<Items> items = itemService.ListAllItems();
        return ResponseEntity.ok(items);
    }

    @PostMapping(value = "/item")
    public ResponseEntity<Items> createItem(@Valid @RequestBody Items item) {
        Items createdItem = itemService.saveItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @DeleteMapping(value = "/item/{itemName}")
    public ResponseEntity<Void> deleteItemByName(@PathVariable String itemName) {
        boolean deleted = itemService.deleteItemByName(itemName);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
