package springbootrestapi.service.impl;

import org.springframework.stereotype.Service;
import springbootrestapi.entity.Post;
import springbootrestapi.exception.ResourceNotFoundException;
import springbootrestapi.payload.PostDto;
import springbootrestapi.repository.PostRepository;
import springbootrestapi.service.PostService;

import java.lang.module.ResolutionException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        // Konvertuje DTO u Entitet
        Post post = mapToEntity(postDto);
        Post newPost = postRepository.save(post);

        //Konvertuje Entitet u DTO
        PostDto postResponse = mapToDTO(newPost);
        return postResponse;
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
    }

    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        // Uzima postove po id-u iz baze podataka
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setOpisposta(postDto.getOpisposta());

        Post updatedPost = postRepository.save(post);
        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        // Uzimamo post iz baze podataka
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
    }

    // Konvertuje Entitet u DTO
    private PostDto mapToDTO(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        postDto.setOpisposta(post.getOpisposta());
        return postDto;
    }

    // Konvertuje DTO u Entitet
    private Post mapToEntity(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setOpisposta(postDto.getOpisposta());
        return post;

    }
}
