package carrot.market.post.service;

import carrot.market.exception.PostNotFoundException;
import carrot.market.post.entity.Post;
import carrot.market.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post findPostById(Long postId) {
        return postRepository.findPostById(postId).orElseThrow(PostNotFoundException::new);
    }
}
