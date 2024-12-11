package burgermap.service;

import burgermap.entity.Review;
import burgermap.exception.review.ReviewNotExistException;
import burgermap.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewLookupService {

    private final ReviewRepository repository;

    public Review findByReviewId(Long reviewId) {
        return repository.findByReviewId(reviewId).orElseThrow(() -> new ReviewNotExistException(reviewId));
    }
}
