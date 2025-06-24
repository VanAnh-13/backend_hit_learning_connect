package com.example.projectbase.service;

import com.example.projectbase.domain.dto.request.GetEmailDto;
import com.example.projectbase.domain.dto.request.VerifyCodeDto;
import org.apache.coyote.BadRequestException;

public interface MailService {
    public String sendMail(GetEmailDto email) throws Exception;
    public String verifyEmail(VerifyCodeDto verifyCode) throws BadRequestException;
}
