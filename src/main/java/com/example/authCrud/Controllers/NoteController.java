package com.example.authCrud.Controllers;

import com.example.authCrud.DTO.NoteDTO;
import com.example.authCrud.entities.Note;
import com.example.authCrud.repositories.UserRepository;
import com.example.authCrud.services.NoteService;
import com.example.authCrud.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserRepository userRepository;

    @Autowired
    public NoteController(NoteService noteService, UserRepository userRepository) {
        this.noteService = noteService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listNotes(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Long userId = getUserIdFromUserDetails(userDetails);
        List<Note> notes = noteService.findAllByUserId(userId);
        model.addAttribute("notes", notes);
        return "notes/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("note", new NoteDTO());
        return "notes/create";
    }

    @PostMapping
    public String createNote(@ModelAttribute("note") NoteDTO noteDTO,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        Long userId = getUserIdFromUserDetails(userDetails);
        try {
            noteService.createNote(noteDTO, userId);
            redirectAttributes.addFlashAttribute("successMessage", "Nota criada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar a nota: " + e.getMessage());
        }
        return "redirect:/notes";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id,
                               @AuthenticationPrincipal UserDetails userDetails,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        Long userId = getUserIdFromUserDetails(userDetails);
        Optional<Note> noteOpt = noteService.findByIdAndUserId(id, userId);
        if (noteOpt.isPresent()) {
            Note note = noteOpt.get();
            NoteDTO noteDTO = new NoteDTO(note.getTitle(), note.getContent());
            model.addAttribute("note", noteDTO);
            model.addAttribute("noteId", id);
            return "notes/edit";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Nota não encontrada.");
        return "redirect:/notes";
    }

    @PostMapping("/{id}")
    public String updateNote(@PathVariable("id") Long id,
                             @ModelAttribute("note") NoteDTO noteDTO,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        Long userId = getUserIdFromUserDetails(userDetails);
        try {
            noteService.updateNote(id, noteDTO, userId);
            redirectAttributes.addFlashAttribute("successMessage", "Nota atualizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar a nota: " + e.getMessage());
        }
        return "redirect:/notes";
    }

    @PostMapping("/{id}/delete")
    public String deleteNote(@PathVariable("id") Long id,
                             @AuthenticationPrincipal UserDetails userDetails,
                             RedirectAttributes redirectAttributes) {
        Long userId = getUserIdFromUserDetails(userDetails);
        try {
            noteService.deleteNote(id, userId);
            redirectAttributes.addFlashAttribute("successMessage", "Nota excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir a nota: " + e.getMessage());
        }
        return "redirect:/notes";
    }

    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        if (userDetails == null) {
            throw new UsernameNotFoundException("Usuário não autenticado");
        }
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return user.getId();
    }
}
