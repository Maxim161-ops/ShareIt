package ru.practicum.shareit.comment;

import ru.practicum.shareit.comment.dto.CommentDto;

import java.time.LocalDateTime;

public class CommentMapper {

    public static CommentDto toDto(Comment comment) {
        if (comment == null) return null;

        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthorName(),
                comment.getCreated()
        );
    }

    public static Comment toComment(CommentDto dto, Long itemId, String authorName) {
        if (dto == null) return null;

        return new Comment(
                null, // id проставится в сервисе
                dto.getText(),
                authorName,
                LocalDateTime.now(),
                itemId
        );
    }
}
