package com.testing.load.auth.dto;

import com.testing.load.user.domain.User;

public record LoginTokenResult(
        String accessToken,
        String refreshToken,
        User user
) {
}
