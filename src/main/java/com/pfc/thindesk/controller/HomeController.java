package com.pfc.thindesk.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pfc.thindesk.entity.Categoria;
import com.pfc.thindesk.entity.Chamado;
import com.pfc.thindesk.entity.Cliente;
import com.pfc.thindesk.entity.Tecnico;
import com.pfc.thindesk.entity.Usuario;
import com.pfc.thindesk.service.CategoriaService;
import com.pfc.thindesk.service.ChamadoService;
import com.pfc.thindesk.service.ClienteService;
import com.pfc.thindesk.service.HorarioAtendimentoService;
import com.pfc.thindesk.service.TecnicoService;
import com.pfc.thindesk.service.UsuarioService;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ChamadoService chamadoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private HorarioAtendimentoService horarioAtendimentoService;
    @Autowired
    private TecnicoService tecnicoService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("layout");
        String fragment = "home :: content";
        log.info("Carregando fragmento: {}", fragment);
        modelAndView.addObject("content", fragment);
        return modelAndView;
    }


    // Chamados
    @GetMapping("/view/chamados")
    public String chamados(
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "prioridade", required = false) String prioridade,
            @RequestParam(value = "tecnicoResponsavelNome", required = false) String tecnicoResponsavelNome,
            Model model,
            Principal principal) {

        // Obtém o usuário logado
        String email = principal.getName();
        Usuario usuarioLogado = usuarioService.buscarUsuarioPorEmail(email).orElse(null);

        List<Chamado> chamados;

        if (usuarioLogado != null && usuarioLogado.getTipoUsuario().equalsIgnoreCase("Tecnico")) {
            // Se o usuário for um técnico, filtra os chamados pelo nome do técnico responsável
            String tecnicoNome = usuarioLogado.getNome();
            chamados = chamadoService.pesquisarChamadosPorTecnicoNome(descricao, status, prioridade, tecnicoNome);
        } else {
            // Caso contrário, busca todos os chamados aplicando os filtros fornecidos
            chamados = chamadoService.pesquisarChamados(descricao, status, prioridade, tecnicoResponsavelNome);
        }

        List<Tecnico> tecnicos = tecnicoService.listarTecnicos();
        List<Usuario> usuarios = usuarioService.listarUsuarios();

        // Ordena os chamados para que os com status "Encerrado" apareçam por último
        chamados.sort((c1, c2) -> {
            String status1 = c1.getStatus() != null ? c1.getStatus().toLowerCase() : "";
            String status2 = c2.getStatus() != null ? c2.getStatus().toLowerCase() : "";
            if (status1.equals("encerrado") && !status2.equals("encerrado")) {
                return 1;
            } else if (!status1.equals("encerrado") && status2.equals("encerrado")) {
                return -1;
            } else {
                return 0;
            }
        });

        Map<String, String> tecnicoMap = tecnicos.stream()
                .collect(Collectors.toMap(Tecnico::getId, Tecnico::getNome));
        Map<String, String> usuarioMap = usuarios.stream()
                .collect(Collectors.toMap(Usuario::getId, Usuario::getNome));

        model.addAttribute("chamados", chamados);
        model.addAttribute("tecnicoMap", tecnicoMap);
        model.addAttribute("usuarioMap", usuarioMap);
        model.addAttribute("tecnicos", tecnicos);
        model.addAttribute("paramDescricao", descricao);
        model.addAttribute("paramStatus", status);
        model.addAttribute("paramPrioridade", prioridade);
        model.addAttribute("paramTecnicoResponsavelNome", tecnicoResponsavelNome);

        return "chamados";
    }
        
    @GetMapping("/cadastro-chamados")
    public String formularioCadastroChamados(Model model) {
        log.info("Acessando formulário de cadastro de chamados.");
        model.addAttribute("chamado", new Chamado());
        model.addAttribute("tecnicos", tecnicoService.listarTecnicos());
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "cadastro-chamados";
    }

    @GetMapping("/atualizar-chamado/{id}")
    public String formularioAtualizarChamado(@PathVariable("id") String id, Model model) {
        Chamado chamado = chamadoService.buscarChamadoPorId(id)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado com ID: " + id));
        model.addAttribute("chamado", chamado);
        model.addAttribute("tecnicos", tecnicoService.listarTecnicos());
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "atualizar-chamado";
    }

    @PutMapping("/atualizar-chamado/{id}")
    public String atualizarChamado(@PathVariable("id") String id, @ModelAttribute("chamado") Chamado chamadoAtualizado) {
        chamadoService.atualizarChamado(id, chamadoAtualizado);
        return "redirect:/view/chamados";
    }

    @PostMapping("/salvar-chamado")
    public String salvarChamado(@ModelAttribute("chamado") Chamado chamado) {
        log.info("Chamado chamado: {}", chamado);
        chamadoService.salvarChamado(chamado); // Salva a categoria no banco
        return "redirect:/view/chamados"; // Redireciona para a lista de chamados
    }

    @DeleteMapping("/view/deletar-chamado/{id}") 
    public String deletarChamado(@PathVariable("id") String id) {
        log.info("Deletando chamado com ID: {}", id);
        chamadoService.deletarChamado(id);
        return "redirect:/view/chamados";
    }

    @GetMapping("/encerrar/{id}")
    public String encerrarChamado(@PathVariable("id") String id) {
        log.info("Encerrando chamado com ID: {}", id);
        chamadoService.encerrarChamado(id);
        return "redirect:/view/chamados";
    }

    // Clientes
    @GetMapping("/view/clientes")
    public String clientes(@RequestParam(value = "nome", required = false) String nome,
                           @RequestParam(value = "empresa", required = false) String empresa,
                           Model model) {
        List<Cliente> clientes = clienteService.pesquisarClientes(nome, empresa);
        List<String> empresas = clienteService.listarEmpresasUnicas();
        model.addAttribute("clientes", clientes);
        model.addAttribute("empresas", empresas);
        model.addAttribute("paramNome", nome);
        model.addAttribute("paramEmpresa", empresa);
        return "clientes";
    }

    @GetMapping("/cadastro-clientes")
    public String formularioCadastroClientes(Model model) {
        log.info("Acessando formulário de cadastro de clientes.");
        model.addAttribute("cliente", new Cliente());
        return "cadastro-clientes";
    }

    @GetMapping("/atualizar-cliente/{id}")
    public String formularioAtualizarClientes(@PathVariable("id") String id, Model model) {
        Cliente cliente = clienteService.buscarClientePorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));
        model.addAttribute("cliente", cliente);
        return "atualizar-cliente";
    }

    @PutMapping("/atualizar-cliente/{id}")
    public String atualizarCliente(@PathVariable("id") String id, @ModelAttribute("cliente") Cliente clienteAtualizado) {
        clienteService.atualizarCliente(id, clienteAtualizado);
        return "redirect:/view/clientes";
    }

    @PostMapping("/salvar-cliente")
    public String salvarCliente(@ModelAttribute("cliente") Cliente cliente) {
        clienteService.salvarCliente(cliente);
        return "redirect:/view/clientes";
    }

    @DeleteMapping("/deletar-cliente/{id}")
    public String deletarCliente(@PathVariable("id") String id) {
        log.info("Deletando cliente com ID: {}", id);
        clienteService.deletarCliente(id); // Chama o serviço para deletar o cliente
        return "redirect:/view/clientes"; // Redireciona para a lista de clientes
    }

    // Técnicos
    @GetMapping("/view/tecnicos")
    public String tecnicos(@RequestParam(value = "nome", required = false) String nome,
                           @RequestParam(value = "especializacao", required = false) String especializacao,
                           @RequestParam(value = "status", required = false) String status,
                           Model model) {
        List<Tecnico> tecnicos = tecnicoService.pesquisarTecnicos(nome, especializacao, status);
        List<String> especializacoes = tecnicoService.listarEspecializacoesUnicas();
        model.addAttribute("tecnicos", tecnicos);
        model.addAttribute("especializacoes", especializacoes);
        model.addAttribute("paramNome", nome);
        model.addAttribute("paramEspecializacao", especializacao);
        model.addAttribute("paramStatus", status);
        return "tecnicos";
    }
    @GetMapping("/cadastro-tecnicos")
    public String formularioCadastroTecnicos(Model model) {
        log.info("Acessando formulário de cadastro de tecnicos.");
        model.addAttribute("tecnico", new Tecnico());
        return "cadastro-tecnicos";
    }

    @DeleteMapping("/deletar-tecnico/{id}")
    public String deletarTecnico(@PathVariable("id") String id) {
        log.info("Deletando tecnico com ID: {}", id);
        tecnicoService.deletarTecnico(id); 
        return "redirect:/view/tecnicos"; 
    }

    @GetMapping("/atualizar-tecnico/{id}")
    public String formularioAtualizarTecnico(@PathVariable("id") String id, Model model) {
        Tecnico tecnico = tecnicoService.buscarTecnicoPorId(id)
                .orElseThrow(() -> new RuntimeException("Tecnico não encontrado com ID: " + id));
        model.addAttribute("tecnico", tecnico);
        return "atualizar-tecnico";
    }

    @PutMapping("/atualizar-tecnico/{id}")
    public String atualizarTecnico(@PathVariable("id") String id, @ModelAttribute("tecnico") Tecnico tecnicoAtualizado) {
        tecnicoService.atualizarTecnico(id, tecnicoAtualizado);
        return "redirect:/view/tecnicos";
    }

    @PostMapping("/salvar-tecnico")
    public String salvarTecnico(@ModelAttribute("tecnico") Tecnico tecnico) {
        log.info("Salvando tecnico: {}", tecnico);
        tecnicoService.salvarTecnico(tecnico); // Salva a categoria no banco
        return "redirect:/view/tecnicos"; // Redireciona para a lista de tecnicos
    }

    // Usuários
    @GetMapping("/view/usuarios")
    public String usuarios(@RequestParam(value = "nome", required = false) String nome,
                           @RequestParam(value = "tipoUsuario", required = false) String tipoUsuario,
                           Model model) {
        List<Usuario> usuarios = usuarioService.pesquisarUsuarios(nome, tipoUsuario);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("paramNome", nome);
        model.addAttribute("paramTipoUsuario", tipoUsuario);
        return "usuarios";
    }

    @GetMapping("/cadastro-usuarios")
    public String formularioCadastroUsuarios(Model model) {
        log.info("Acessando formulário de cadastro de usuarios.");
        model.addAttribute("usuario", new Usuario());
        return "cadastro-usuarios";
    }

    @GetMapping("/atualizar-usuario/{id}")
    public String formularioAtualizarUsuario(@PathVariable("id") String id, Model model) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado com ID: " + id));
        model.addAttribute("usuario", usuario);
        return "atualizar-usuario";
    }

    @PutMapping("/atualizar-usuario/{id}")
    public String atualizarUsuario(@PathVariable("id") String id, @ModelAttribute("usuario") Usuario usuarioAtualizado) {
        Usuario usuarioExistente = usuarioService.buscarUsuarioPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado com ID: " + id));
        
        usuarioService.atualizarUsuario(id, usuarioAtualizado, usuarioExistente.getSenha());
        return "redirect:/view/usuarios";
    }

    @PostMapping("/salvar-usuarios")
    public String salvarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        log.info("Salvando usuario: {}", usuario);
        usuarioService.salvarUsuario(usuario); // Salva a categoria no banco
        return "redirect:/view/usuarios"; // Redireciona para a lista de usuarios
    }

    @DeleteMapping("/deletar-usuario/{id}")
    public String deletarUsuario(@PathVariable("id") String id) {
        log.info("Deletando usuario com ID: {}", id);
        usuarioService.deletarUsuario(id); 
        return "redirect:/view/usuarios"; 
    }

     // Categorias
     @GetMapping("/view/categorias")
     public String categorias(@RequestParam(value = "nome", required = false) String nome,
                              @RequestParam(value = "tipo", required = false) String tipo,
                              @RequestParam(value = "descricao", required = false) String descricao,
                              Model model) {
         List<Categoria> categorias = categoriaService.pesquisarCategorias(nome, tipo, descricao);
         model.addAttribute("categorias", categorias);
         model.addAttribute("paramNome", nome);
         model.addAttribute("paramTipo", tipo);
         model.addAttribute("paramDescricao", descricao);
         return "categorias";
     }

    @GetMapping("/cadastro-categorias")
    public String formularioCadastroCategorias(Model model) {
        log.info("Acessando formulário de cadastro de categorias.");
        model.addAttribute("categoria", new Categoria());
        return "cadastro-categorias";
    }

    @GetMapping("/atualizar-categoria/{id}")
    public String formularioAtualizarCategoria(@PathVariable("id") String id, Model model) {
        Categoria categoria = categoriaService.buscarCategoriaPorId(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + id));
        model.addAttribute("categoria", categoria);
        return "atualizar-categorias";
    }

    @PutMapping("/atualizar-categoria/{id}")
    public String atualizarCategoria(@PathVariable("id") String id, @ModelAttribute("categoria") Categoria categoriaAtualizada) {
        categoriaService.atualizarCategoria(id, categoriaAtualizada);
        return "redirect:/view/categorias";
    }
    

    @PostMapping("/salvar-categoria")
    public String salvarCategoria(@ModelAttribute("categoria") Categoria categoria) {
        log.info("Salvando categoria: {}", categoria);
        categoriaService.salvarCategoria(categoria); // Salva a categoria no banco
        return "redirect:/view/categorias"; // Redireciona para a lista de categorias
    }

    @DeleteMapping("/deletar-categoria/{id}")
    public String deletarCategoria(@PathVariable("id") String id) {
        log.info("Deletando categoria com ID: {}", id);
        categoriaService.deletarCategoria(id); 
        return "redirect:/view/categorias"; 
    }

    
}
