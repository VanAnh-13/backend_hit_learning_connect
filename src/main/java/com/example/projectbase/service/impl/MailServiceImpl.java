package com.example.projectbase.service.impl;

import com.example.projectbase.domain.dto.common.DataMailDto;
import com.example.projectbase.domain.dto.request.GetEmailDto;
import com.example.projectbase.domain.dto.request.VerifyCodeDto;
import com.example.projectbase.repository.UserRepository;
import com.example.projectbase.service.MailService;
import com.example.projectbase.util.SendMailUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Duration;

@Service
public class MailServiceImpl implements MailService {

    private static final int   MAX_SEND_COUNT_PER_HOUR = 5;
    private static final Duration COUNT_TTL = Duration.ofHours(1);
    private static final Duration BAN_TTL   = Duration.ofMinutes(20);
    private static final Duration CODE_TTL  = Duration.ofMinutes(5);

    @Autowired
    private SendMailUtil sendMailUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String sendMail(GetEmailDto email) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String ip = request.getRemoteAddr();

        String banKey = "mail:ban:" + ip;
        if (redisTemplate.hasKey(banKey)) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Too much request, comeback in 20 minutes"
            );
        }

        String countKey = "mail:count:" + ip;
        Long count = redisTemplate.opsForValue().increment(countKey);
        if (count == 1L) {
            redisTemplate.expire(countKey, COUNT_TTL);
        }

        if (count > MAX_SEND_COUNT_PER_HOUR) {
            redisTemplate.opsForValue().set(banKey, "1", COUNT_TTL);
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Too much request, Your IP has Ban in 20 minutes."
            );
        }

        String code = String.format(
                "%06d",
                new SecureRandom().nextInt(1_000_000)
        );

        DataMailDto data = DataMailDto.builder()
                .to(email.getEmail())
                .content("Your Verification Code:" + code)
                .subject("Verification Code")
                .build();

        sendMailUtil.sendMail(data);

        String codeKey =  "mail:code:" + email.getEmail();
        redisTemplate.delete(codeKey);
        redisTemplate.opsForValue().set(codeKey, code, CODE_TTL);

        return "Send mail successfully";
    }

    @Override
    public String verifyEmail(VerifyCodeDto verifyCode) throws BadRequestException {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new BadRequestException("Unable to determine client IP");
        }
        HttpServletRequest request = attrs.getRequest();
        String ip = request.getRemoteAddr();

        String banKey = "mail:ban:" + ip;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(banKey))) {
            throw new BadRequestException("Too many failed attempts. Your IP is blocked for 20 minutes.");
        }

        String codeKey = "mail:code:" + verifyCode.getEmail();
        String cached = redisTemplate.opsForValue().get(codeKey);
        if (cached == null) {
            throw new BadRequestException("The code has expired or was never sent.");
        }

        if (!cached.equals(verifyCode.getCode())) {
            String attemptsKey = "mail:attempts:" + ip;
            Long fails = redisTemplate.opsForValue().increment(attemptsKey);
            if (fails == 1L) {
                redisTemplate.expire(attemptsKey, Duration.ofHours(1));
            }

            if (fails >= 5) {
                redisTemplate.opsForValue().set(banKey, "1", Duration.ofMinutes(20));
                redisTemplate.delete(attemptsKey);
                throw new BadRequestException("Too many incorrect codes. Your IP has been blocked for 20 minutes.");
            }

            throw new BadRequestException("Invalid verification code. You have failed " + fails + " times.");
        }


        redisTemplate.delete(codeKey);
        redisTemplate.delete("mail:attempts:" + ip);


        try {
            userRepository.updatePasswordByEmail(
                    verifyCode.getEmail(),
                    passwordEncoder.encode(verifyCode.getNewPassword())
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not update password");
        }

        return "Password changed successfully";
    }



}
