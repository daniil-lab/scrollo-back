package com.inst.base.request.chat;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class SendMessageRequest {
    @NotNull
    private UUID chatId;

    @NotBlank
    @Length(min = 1, max = 2048)
    private String text;

    @Size(max = 5)
    private List<MultipartFile> attachments;
}
