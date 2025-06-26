package com.pedrocasseb.spotify_clone.catalogcontext.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pedrocasseb.spotify_clone.catalogcontext.application.SongService;
import com.pedrocasseb.spotify_clone.catalogcontext.application.dto.ReadSongInfoDTO;
import com.pedrocasseb.spotify_clone.catalogcontext.application.dto.SaveSongDTO;
import com.pedrocasseb.spotify_clone.catalogcontext.application.dto.SongContentDTO;
import com.pedrocasseb.spotify_clone.usercontext.application.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class SongResource {
    
    // ... outros códigos ...

    private final SongService songService;

    private final Validator validator;

    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SongResource(SongService songService, Validator validator, UserService userService) {
        this.songService = songService;
        this.validator = validator;
        this.userService = userService;
    }

    @PostMapping(value = "/songs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReadSongInfoDTO> add(
            @RequestPart(name = "cover") MultipartFile cover,
            @RequestPart(name = "file") MultipartFile file,
            @RequestPart(name = "dto") String saveSongDTOString) throws IOException {
        
        SaveSongDTO saveSongDTO = objectMapper.readValue(saveSongDTOString, SaveSongDTO.class);
        saveSongDTO = new SaveSongDTO(
            saveSongDTO.title(), 
            saveSongDTO.author(), 
            cover.getBytes(), 
            cover.getContentType(), 
            file.getBytes(), 
            file.getContentType()
        );

        Set<ConstraintViolation<SaveSongDTO>> violations = validator.validate(saveSongDTO);
        
        if (!violations.isEmpty()) {  // Se há violações
            String violationsJoined = violations.stream()
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .collect(Collectors.joining(", "));
                
            ProblemDetail validationIssue = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, 
                "Validation errors for the fields: " + violationsJoined
            );
            return ResponseEntity.badRequest().body(null);
        }
        
        // Se não há violações, cria a música
        return ResponseEntity.ok(songService.create(saveSongDTO));
    }

    @GetMapping("/songs")
    public ResponseEntity<List<ReadSongInfoDTO>> getAll() {
        return ResponseEntity.ok(songService.getAll());
    }
}