package ru.practicum.shareit.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;

    @NotBlank(message = "Текст отзыва не может быть пустым")
    private String text;

    @NotBlank(message = "Имя автора обязательно")
    private String authorName;

    @NotNull(message = "Дата создания обязательна")
    private LocalDateTime created;
}
