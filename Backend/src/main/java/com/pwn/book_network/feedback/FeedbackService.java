package com.pwn.book_network.feedback;

import com.pwn.book_network.book.Book;
import com.pwn.book_network.book.BookRepository;
import com.pwn.book_network.common.PageResponse;
import com.pwn.book_network.exception.OperationProhibitted;
import com.pwn.book_network.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedBackRepository feedBackRepository;

    public Integer save(FeedbackRequest request, Authentication connectedUser) {

        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(()-> new EntityNotFoundException("Book not found :"+ request.bookId())); //get the bookId

        if(book.isArchived() || !book.isShareable()){
            throw new OperationProhibitted("not allowed "); //checks
        }
        User user= ((User) connectedUser.getPrincipal()); // get user

        if(Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationProhibitted("Not allowed"); // checks
        }

        FeedBack feedBack=feedbackMapper.toFeedback(request); //map feedback request to feedback response
        return feedBackRepository.save(feedBack).getId(); // save the feedback

    }

    public PageResponse<FeedbackResponse> findAllFeedbackByBook(Integer bookId, int page, int size, Authentication connectedUser) {

        //same paging
        Pageable pageable = PageRequest.of(page,size);

        User user = ((User) connectedUser.getPrincipal());

        //custom query
        Page<FeedBack> feedbacks = feedBackRepository.findAllByBookId(bookId,pageable);

        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(f-> feedbackMapper.toFeedbackResponse(f, user.getId()))
                .toList();
        //list of feedbacks and userIds mapped

        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
                //return the response
        );
    }
}
