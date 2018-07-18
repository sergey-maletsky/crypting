package com.snm.security.controller;

import com.snm.security.dto.AuthDto;
import com.snm.security.dto.JsonResult;
import com.snm.security.service.AuthService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.snm.security.dto.JsonResult.ErrorCode.AUTH_ERROR;
import static com.snm.security.dto.JsonResult.ErrorCode.NO_ERROR;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    private Map<String, String> uuidRandomNumberMap = new ConcurrentHashMap<>();

    @ApiOperation(value = "Getting a random key")
    @RequestMapping(method = RequestMethod.GET, value = "key")
    public ResponseEntity<AuthDto> sendRandomNumber() {
        String uuid = UUID.randomUUID().toString();
        String randomValue = RandomStringUtils.randomAlphanumeric(32);
        uuidRandomNumberMap.put(uuid, randomValue);
        AuthDto authDto = new AuthDto(uuid, randomValue);

        return ResponseEntity.ok(authDto);
    }

    @ApiOperation(value = "Sign in a user by kep")
    @RequestMapping(method = RequestMethod.POST, value = "signInByKep")
    public ResponseEntity<JsonResult> authByKep(@RequestBody AuthDto authDto) {
        if (!uuidRandomNumberMap.containsKey(authDto.getUuid())) {
            log.error("The uuid is incorrect");
            JsonResult result = new JsonResult<>(AUTH_ERROR, "Operation can't be finished. Try again, please");
            return ResponseEntity.badRequest().body(result);
        }

        JsonResult result = authService.checkUserCertificate(authDto, uuidRandomNumberMap.get(authDto.getUuid()));

        return createResponseEntity(result);
    }

    private ResponseEntity createResponseEntity(JsonResult result) {
        if (!result.getErrorCode().equals(NO_ERROR.getCode())) {
            return ResponseEntity.badRequest().body(result);
        } else {
            return ResponseEntity.ok(result);
        }
    }
}
