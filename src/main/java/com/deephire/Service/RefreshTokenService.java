package com.deephire.Service;


import com.deephire.Models.RefreshToken;
import com.deephire.Repositories.RefreshTokenRepository;
import com.deephire.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${deepHire.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }


    public RefreshToken createRefreshToken(Long id) {
        // Check if the user already has a refresh token
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUserId(id);

        if (existingToken.isPresent()) {
            RefreshToken refreshToken = existingToken.get();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpryDate(Instant.now().plusMillis(refreshTokenDurationMs));
            return refreshTokenRepository.save(refreshToken); // update existing token
        }

        // If not, create a new one
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(id).orElseThrow());
        refreshToken.setExpryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

}
