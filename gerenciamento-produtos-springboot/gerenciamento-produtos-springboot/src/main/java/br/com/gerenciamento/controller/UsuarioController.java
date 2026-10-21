package br.com.gerenciamento.controller;

import br.com.gerenciamento.exception.ServiceExc;
import br.com.gerenciamento.model.Usuario;
import br.com.gerenciamento.service.ServiceUsuario;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UsuarioController {

    private final ServiceUsuario serviceUsuario;

    public UsuarioController(ServiceUsuario serviceUsuario) {
        this.serviceUsuario = serviceUsuario;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView("login/login");
        mv.addObject("usuario", new Usuario());
        return mv;
    }

    @GetMapping("/cadastro")
    public ModelAndView cadastro() {
        ModelAndView mv = new ModelAndView("login/cadastro");
        mv.addObject("usuario", new Usuario());
        return mv;
    }

    @PostMapping("/salvarUsuario")
    public ModelAndView salvarUsuario(@Valid Usuario usuario, BindingResult result) {
        ModelAndView mv = new ModelAndView("login/cadastro");
        if (result.hasErrors()) {
            mv.addObject("usuario", usuario);
            return mv;
        }
        try {
            serviceUsuario.salvarUsuario(usuario);
            mv.setViewName("redirect:/login");
            return mv;
        } catch (Exception e) {
            mv.addObject("usuario", usuario);
            mv.addObject("msg", e.getMessage());
            return mv;
        }
    }

    @PostMapping("/login")
    public ModelAndView efetuarLogin(@ModelAttribute Usuario usuario, HttpSession session) {
        ModelAndView mv = new ModelAndView("login/login");
        mv.addObject("usuario", usuario);
        try {
            Usuario logado = serviceUsuario.loginUser(usuario.getUser(), usuario.getSenha());
            if (logado == null) {
                mv.addObject("msg", "Usuário ou senha inválidos.");
                return mv;
            }
            session.setAttribute("usuarioLogado", logado);
            return new ModelAndView("redirect:/index");
        } catch (ServiceExc e) {
            mv.addObject("msg", e.getMessage());
            return mv;
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
