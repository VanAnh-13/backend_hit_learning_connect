package com.example.projectbase.domain.dto.request.storage;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DownloadRequestDto {
    String pathName;
}
