package org.example.controllers;
import org.example.services.ItemService;
import org.example.tables.Champions;
import org.example.tables.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
@CrossOrigin(origins = "http://localhost:3000")
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
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/item/{itemName}")
    public ResponseEntity<Items> updateItem(@PathVariable String itemName, @Valid @RequestBody Items updatedItem) {
        Optional<Items> result = itemService.updateItem(itemName, updatedItem);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/item")
    public ResponseEntity<Page<Items>> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Items> items = itemService.listAllItemsPaginated(pageable);
        return ResponseEntity.ok(items);
    }
}
