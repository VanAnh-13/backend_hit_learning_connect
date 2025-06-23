package com.example.projectbase.domain.dto.common;

import com.example.projectbase.util.SendMailUtil;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

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