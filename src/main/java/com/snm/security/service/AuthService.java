package com.snm.security.service;

import com.snm.security.dto.AuthDto;
import com.snm.security.dto.JsonResult;
import com.snm.security.dto.RegistrationData;

public interface AuthService {

    JsonResult checkUserCertificate(AuthDto authDto, String randomValue);
}
