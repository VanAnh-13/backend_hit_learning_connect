package com.example.projectbase.domain.dto.request.storage;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteFileRequestDto {
    private String url;
}
