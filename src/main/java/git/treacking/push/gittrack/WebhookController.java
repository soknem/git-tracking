package git.treacking.push.gittrack;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/webhook")
public class WebhookController {

    private final TelegramService telegramService;

    @PostMapping("/springboot")
    public void handleSpringBootWebhook(@RequestBody Map<String, Object> payload) {
        processWebhookPayload(payload, "Spring Boot");
    }

    @PostMapping("/nextjs")
    public void handleNextJsWebhook(@RequestBody Map<String, Object> payload) {
        processWebhookPayload(payload, "Next.js");
    }


    private void processWebhookPayload(Map<String, Object> payload, String projectName) {
        String ref = (String) payload.get("ref");
        String branch = ref.replace("refs/heads/", "");

        List<Map<String, Object>> commits = (List<Map<String, Object>>) payload.get("commits");

        for (Map<String, Object> commit : commits) {
            Map<String, String> author = (Map<String, String>) commit.get("author");
            String authorName = author.get("name");
            String message = (String) commit.get("message");
            String dateTime = (String) commit.get("timestamp");

            telegramService.sendGitPushInfo(projectName, authorName, message, branch, dateTime);
        }
    }
}
