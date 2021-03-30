package com.jaeho.sonarservice.domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private int id;

    @NonNull
    private String userId;

    @NonNull
    private String password;

    @NonNull
    private String name;
}
