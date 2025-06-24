package com.example.projectbase.domain.dto.common;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class DataMailDto {

  private String to;

  private String subject;

  private String content;

  private Map<String, Object> properties;

}