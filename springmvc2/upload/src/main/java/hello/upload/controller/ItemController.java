package hello.upload.controller;
import hello.upload.domain.UploadFile;
import hello.upload.domain.Item;
import hello.upload.domain.ItemRepository;
import hello.upload.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form) {
        return "item-form";
    }

    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile()); // 파일 하나 처리
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles()); // 파일 여러개 처리

        // 데이터베이스에 저장
        // 파일은 S3같은 별도의 Storage에 저장하고 DB에는 경로만 저장합니다.
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", item.getId());
        return "redirect:/items/{itemId}";
    }

    // 저장한 아이템을 보여주는 곳
    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);
        return "item-view";
    }

    // 버튼을 누르면 해당 이미지를 유저가 내려받음
    @ResponseBody // 반환데이터가 json임을 의미
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        // UrlResource를 하면 해당 파일을 다운받음
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    // 첨부파일 다운로드(이미지 말고 파일)
    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
        Item item = itemRepository.findById(itemId);
        String storeFileName = item.getAttachFile().getStoreFileName(); // 실제 파일을 찾기 위해
        String uploadFileName = item.getAttachFile().getUploadFileName(); // 사용자가 다운로드 받을 때는 본인이 저장한 이름으로 다운받아야 하니깐
        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        log.info("uploadFileName={}", uploadFileName);

        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8); // 인코딩
        // 규약, 이걸 넣으면 파일을 다운로드 하겠다는 의미
        // 헤더가 없으면 다운로드가 아니라 해당 파일의 내용이 보임
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition) // 헤더를 함께 보냄
                .body(resource);
    }

}