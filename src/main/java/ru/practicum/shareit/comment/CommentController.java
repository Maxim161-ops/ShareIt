package ru.practicum.shareit.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.List;

@RestController
@RequestMapping("/items/{itemId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody CommentDto commentDto) {
        return commentService.addComment(userId, itemId, commentDto);
    }

    @GetMapping
    public List<CommentDto> getComments(@PathVariable Long itemId) {
        return commentService.getCommentsByItem(itemId);
    }
}
