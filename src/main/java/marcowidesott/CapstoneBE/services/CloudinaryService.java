package marcowidesott.CapstoneBE.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${CLOUDINARY_NAME}") String cloudName,
            @Value("${CLOUDINARY_KEY}") String apiKey,
            @Value("${CLOUDINARY_SECRET}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

  public String uploadFile(MultipartFile file) throws IOException {
    if (file == null || file.isEmpty()) {
        throw new IllegalArgumentException("File non valido");
    }

    try {
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("resource_type", "auto")
        );

        Object secureUrl = uploadResult.get("secure_url");

        if (secureUrl == null) {
            throw new IOException("URL sicuro non restituito da Cloudinary");
        }

        return secureUrl.toString();
    } catch (Exception e) {
        throw new IOException("Errore durante l'upload su Cloudinary: " + e.getMessage(), e);
    }
}


    public String uploadFileWithTransformation(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "transformation", new Transformation<>().width(300).height(300).crop("fill").gravity("face")
        ));
        return uploadResult.get("secure_url").toString();
    }
}
