package com.example.sweater.controller;
import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.repository.MessageRepo;
import com.example.sweater.service.UserSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo; //инжект MessageRepo
    @Autowired
    private UserSevice userSevice;
    //внедрение значение из application.properties
    @Value("${upload.path}")
    private String uploadPath;

    //при запуске app с mustache стартовой страницей будет home.mustache
    @GetMapping("/")
    public String home(Map<String, Object> model) {
        return "home";
    }
    //main.mustache
    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Message> messages = messageRepo.findAll();
        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }

    //main.mustache, обработка значений введенных в форму RequestParam забюирает занченеие по имени из ФОРМЫ
    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
            @Valid Message message, BindingResult bindingResult,
            Model model,  @RequestParam("file") MultipartFile file) throws IOException {
        message.setAuthor(user);
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorMap);
            model.addAttribute("message", message);
        } else {
            saveFile(message, file);
            model.addAttribute("message", null);
            messageRepo.save(message);   //сохр Message в БД исп  messageRepo
        }
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages); //сохр Message в модель для отображения исп mustache
        return "main";
    }
    private void saveFile(Message message, MultipartFile file) throws IOException {
        if(file !=null && !file.getOriginalFilename().isEmpty()){
            File uploadDir = new File(uploadPath); //получаем путь
            if (!uploadDir.exists()) {
                uploadDir.mkdir(); //если не существуют создаем
            }
            String uuidFile = UUID.randomUUID().toString(); //создание уникального имени файла во избежание коллизиций
            String resultFilename = uuidFile + "." + file.getOriginalFilename(); //конкатенируем уникальное имя с именем файла от пользователя
            file.transferTo(new File(uploadPath + "/"+ resultFilename));
            message.setFilename(resultFilename);
        }
    }

    @GetMapping("/user-messages/{user}")
    public String userMessges(@AuthenticationPrincipal User currentUser,
            @PathVariable User user,  Model model,
            @RequestParam(required = false) Message message ) {
        Set<Message> messages = user.getMessages();
        model.addAttribute("userChannel", user);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("messages", messages);
        model.addAttribute("message", message);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable int user,
            //@PathVariable String userId,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (message.getAuthor().equals(currentUser)) {
            if (!StringUtils.isEmpty(text)) {
                message.setText(text);
            }
            if (!StringUtils.isEmpty(tag)) {
                message.setTag(tag);
            }
            saveFile(message, file);
            messageRepo.save(message);
        }
        return "redirect:/user-messages/" + user;
    }
}