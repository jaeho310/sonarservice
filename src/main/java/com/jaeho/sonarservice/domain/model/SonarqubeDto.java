package com.jaeho.sonarservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SonarqubeDto {
    int id;

    String sonarqubeName;

    String sonarqubeKey;

    int mavenFileId;
}
