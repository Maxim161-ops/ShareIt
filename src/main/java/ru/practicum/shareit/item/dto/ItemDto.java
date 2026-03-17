package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(message = "Имя обязательно")
    private String name;

    @NotBlank(message = "Описание обязательно")
    private String description;

    private Long owner;

    @NotNull(message = "Статус доступности обязателен")
    private Boolean available;

    private Long request;
}
