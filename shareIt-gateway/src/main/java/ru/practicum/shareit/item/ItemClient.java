package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl,
                      RestTemplateBuilder builder) {

        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .build());
    }

    public ResponseEntity<Object> createItem(Long userId, Object dto) {
        return post("", userId, dto);
    }

    public ResponseEntity<Object> getItem(Long userId, Long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllItems(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> updateItem(Long userId, Long itemId, Object dto) {
        return patch("/" + itemId, userId, dto);
    }

    public ResponseEntity<Object> searchItems(String text) {
        Map<String, Object> params = Map.of("text", text);
        return get("/search", null, params);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, Object dto) {
        return post("/" + itemId + "/comment", userId, dto);
    }
}
