package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;


import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {

    private final RestTemplate rest;

    private static final String API_PREFIX = "/requests";

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl,
                             RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
        this.rest = builder.build();
    }

    public ResponseEntity<Object> createRequest(Long userId, ItemRequestDto dto) {
        return post("", userId, dto);
    }

    public ResponseEntity<Object> getUserRequests(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getAllRequests(Long userId, int from, int size) {
        Map<String, Object> params = Map.of(
                "from", from,
                "size", size
        );
        return get("all?from={from}&size={size}", userId, params);
    }

    public ResponseEntity<Object> getRequestById(Long userId, Long requestId) {
        return get("/" + requestId, userId);
    }
}
