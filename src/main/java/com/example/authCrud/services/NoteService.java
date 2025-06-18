package com.example.authCrud.services;

import com.example.authCrud.DTO.NoteDTO;
import com.example.authCrud.entities.Note;
import com.example.authCrud.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;  

    public Note createNote(NoteDTO noteDTO, Long userId) {
        Note note = new Note();
        note.setTitle(noteDTO.getTitle());
        note.setContent(noteDTO.getContent());
        note.setUserId(userId);
        return noteRepository.save(note);
    }

    public List<Note> findAllByUserId(Long userId) {
        return noteRepository.findByUserId(userId);
    }

    public Optional<Note> findByIdAndUserId(Long noteId, Long userId) {
        return noteRepository.findByIdAndUserId(noteId, userId);
    }

    public Optional<Note> updateNote(Long noteId, NoteDTO noteDTO, Long userId) {
        Optional<Note> noteOpt = findByIdAndUserId(noteId, userId);
        if (noteOpt.isPresent()) {
            Note note = noteOpt.get();
            note.setTitle(noteDTO.getTitle());
            note.setContent(noteDTO.getContent());
            noteRepository.save(note);
            return Optional.of(note);
        }
        return Optional.empty();
    }

    public boolean deleteNote(Long noteId, Long userId) {
        Optional<Note> noteOpt = findByIdAndUserId(noteId, userId);
        if (noteOpt.isPresent()) {
            noteRepository.delete(noteOpt.get());
            return true;
        }
        return false;
    }
}
