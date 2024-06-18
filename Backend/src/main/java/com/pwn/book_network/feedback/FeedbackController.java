package com.pwn.book_network.feedback;

import com.pwn.book_network.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedbacks")
@AllArgsConstructor
@Tag(name = "Feedback")
public class FeedbackController {

    private final FeedbackService service;

    @PostMapping
    public ResponseEntity<Integer> saveFeedback(
            @Valid @RequestBody FeedbackRequest request, // valid request of FeedbackRequest type
            Authentication connectedUser // user
    ){
        return ResponseEntity.ok(service.save(request, connectedUser));
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbacksByBook( //Paging is added considering the number of feedbacks can be high
            @PathVariable("book-id") Integer bookId,
            @RequestParam(name="page", defaultValue = "0", required = false) int page,
            @RequestParam(name="size", defaultValue = "0", required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.findAllFeedbackByBook(bookId,page,size,connectedUser));
        //return page responses
    }

}
















