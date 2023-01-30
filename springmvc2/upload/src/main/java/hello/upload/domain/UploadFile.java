package hello.upload.domain;
import lombok.Data;
@Data
public class UploadFile {
    private String uploadFileName; // 사용자가 업로드 한 파일이름
    private String storeFileName; // 실제 서버에 저장할 파일이름 -> 사용자가 같은 이름으로 올릴 가능성이 있기 때문
    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}