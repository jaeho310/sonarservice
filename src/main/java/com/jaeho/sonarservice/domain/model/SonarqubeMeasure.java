package com.jaeho.sonarservice.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SonarqubeMeasure {

    private int bugs;

    private int vulnerability;

    private int codeSmell;

    private int loc;

    private double coverage;

    private double duplicated;

}
