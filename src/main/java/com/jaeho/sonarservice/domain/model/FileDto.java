package com.jaeho.sonarservice.domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDto {
    private int id;

    @NonNull
    private String fileName;

    @NonNull
    private int userId;

    @NonNull
    private String projectName;
}
