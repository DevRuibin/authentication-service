package org.andy.chatfybackend.auth.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@ToString
@Getter
public class ErrorDetails {
    private String message;
    @Builder.Default
    private String details = "";
}