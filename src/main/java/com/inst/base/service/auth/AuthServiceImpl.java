package com.inst.base.service.auth;

import com.inst.base.config.JwtProvider;
import com.inst.base.config.RefreshJwtProvider;
import com.inst.base.entity.auth.ConfirmationState;
import com.inst.base.entity.auth.EmailConfirmation;
import com.inst.base.entity.user.User;
import com.inst.base.repository.auth.EmailConfirmationRepository;
import com.inst.base.repository.user.UserRepository;
import com.inst.base.request.auth.*;
import com.inst.base.response.auth.BaseAuthResponse;
import com.inst.base.response.auth.StartEmailConfirmationResponse;
import com.inst.base.util.PasswordValidator;
import com.inst.base.util.RequestIPExtractor;
import com.inst.base.util.ServiceException;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Log4j2(topic = "Auth Service")
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private RefreshJwtProvider refreshJwtProvider;

    @Autowired
    private EmailConfirmationRepository emailConfirmationRepository;

    @Autowired
    private RequestIPExtractor ipExtractor;

    @Override
    public StartEmailConfirmationResponse startEmailConfirmation(StartEmailConfirmationRequest request) {
        Optional<EmailConfirmation> foundConfirmation = emailConfirmationRepository.findByEmailAndState(request.getEmail(), ConfirmationState.WAIT_CONFIRM);

        if(foundConfirmation.isPresent()) {
            EmailConfirmation confirmation = foundConfirmation.get();

            confirmation.setCode("%04d".formatted(new Random().nextInt(10000)));
            confirmation.setEnterCount(0);
            confirmation.setExpiredAt(Instant.now());

            emailConfirmationRepository.save(confirmation);

            StartEmailConfirmationResponse response = new StartEmailConfirmationResponse();
            response.setId(confirmation.getId());

            log.info("""
                    На почту %s был повторно отправлен код подтверждения: %s
                    IP адрес: %s
                    """.formatted(confirmation.getEmail(), confirmation.getCode(), ipExtractor.extract()));

            return response;
        } else {
            EmailConfirmation confirmation = new EmailConfirmation();

            confirmation.setEmail(request.getEmail());
            confirmation.setCode("%04d".formatted(new Random().nextInt(10000)));
            confirmation.setExpiredAt(Instant.now());

            emailConfirmationRepository.save(confirmation);

            StartEmailConfirmationResponse response = new StartEmailConfirmationResponse();
            response.setId(confirmation.getId());

            log.info("""
                    На почту %s был отправлен код подтверждения: %s
                    IP адрес: %s
                    """.formatted(confirmation.getEmail(), confirmation.getCode(), ipExtractor.extract()));

            return response;
        }
    }

    @Override
    public Boolean submitEmailConfirmation(SubmitEmailConfirmationRequest request) {
        EmailConfirmation confirmation = emailConfirmationRepository.findById(request.getConfirmationId()).orElseThrow(() -> {
            log.info("""
                    Попытка найти подтверждение почты с несуществующим ID.
                    IP адрес: %s
                    """
                    .formatted(ipExtractor.extract()));

            throw new ServiceException("Запрос на подтверждение не найден", HttpStatus.NOT_FOUND);
        });

        if(confirmation.getExpiredAt().plus(30, ChronoUnit.MINUTES).isBefore(Instant.now())) {
            confirmation.setState(ConfirmationState.EXPIRED);

            emailConfirmationRepository.save(confirmation);

            log.info("""
                    Подтверждение почты %s с кодом %s истекло по времени. ID подтверждения %s
                    IP адрес: %s
                    """
                    .formatted(confirmation.getEmail(), confirmation.getCode(), confirmation.getId(), ipExtractor.extract()));

            throw new ServiceException("Срок действия запроса на подтверждения истек. Попробуйте начать подтверждение с начала.", HttpStatus.BAD_REQUEST);
        }

        if(confirmation.getState() != ConfirmationState.WAIT_CONFIRM) {
            log.info("""
                    Попытка ввести код подтверждения для подтверждения почты %s с кодом %s с невалидным статусом. Требуемый: WAIT_CONFIRM. Действующий: %s. ID подтверждения %s.
                    IP адрес: %s
                    """
                    .formatted(confirmation.getEmail(), confirmation.getCode(), confirmation.getState().name(), confirmation.getId(), ipExtractor.extract()));

            throw new ServiceException("Невалидный запрос подтверждения. Попробуйте начать подтверждения с начала.", HttpStatus.BAD_REQUEST);
        }

        if(confirmation.getEnterCount() >= 3) {
            log.info("""
                    Подтверждение почты %s с кодом %s. Попытка ввести код более 3 раз. ID подтверждения %s
                    IP адрес: %s
                    """
                    .formatted(confirmation.getEmail(), confirmation.getCode(), confirmation.getId(), ipExtractor.extract()));

            throw new ServiceException("Использовано максимальное количество попыток", HttpStatus.BAD_REQUEST);
        }

        if(Integer.parseInt(confirmation.getCode().toString()) != Integer.parseInt(request.getCode().replaceAll("\\s+", ""))) {
            if(confirmation.getEnterCount() >= 2) {
                confirmation.setEnterCount(confirmation.getEnterCount() + 1);
                confirmation.setState(ConfirmationState.ENTER_CODE_LIMIT);

                emailConfirmationRepository.save(confirmation);

                log.info("Подтверждение почты %s с кодом %s. Были исчерпаны все попытки ввода. ID подтверждения %s"
                        .formatted(confirmation.getEmail(), confirmation.getCode(), confirmation.getId()));

                throw new ServiceException("Вы использовали все попытки на ввод кода. Попробуйте начать подтверждение с начала.", HttpStatus.BAD_REQUEST);
            } else {
                confirmation.setEnterCount(confirmation.getEnterCount() + 1);

                emailConfirmationRepository.save(confirmation);

                log.info("Подтверждение почты %s с кодом %s. Был неверно введен код. ID подтверждения %s. Было введено %s"
                        .formatted(confirmation.getEmail(), confirmation.getCode(), confirmation.getId(), request.getCode()));

                throw new ServiceException("Вы ввели неверный код подтверждения.", HttpStatus.BAD_REQUEST);
            }
        }

        log.info("Подтверждение почты %s с кодом %s успешно выполнено. ID подтверждения %s."
                .formatted(confirmation.getEmail(), confirmation.getCode(), confirmation.getId()));

        confirmation.setState(ConfirmationState.CONFIRMED);

        emailConfirmationRepository.save(confirmation);

        return true;
    }

    @Override
    public User signInByEmailConfirmation(SignInByEmailConfirmationRequest request) {
        EmailConfirmation emailConfirmation = emailConfirmationRepository.findByIdAndState(request.getConfirmationId(), ConfirmationState.CONFIRMED).orElseThrow(() -> {
            log.info("Попытка найти несуществующее подтверждение почты");
            throw new ServiceException("Почта не подтверждена или подтверждения не существует.", HttpStatus.BAD_REQUEST);
        });

        if(emailConfirmation.getExpiredAt().plus(30, ChronoUnit.MINUTES).isAfter(Instant.now())) {
            throw new ServiceException("Подтверждение почты истекло. Подтвердите почту снова для регистрации.", HttpStatus.BAD_REQUEST);
        }

        userRepository.findByLogin(request.getLogin()).ifPresent((val) -> {
            throw new ServiceException("User with given login already exists", HttpStatus.BAD_REQUEST);
        });

        userRepository.findByEmailDataEmail(request.getEmail()).ifPresent((val) -> {
            throw new ServiceException("User with given login already exists", HttpStatus.BAD_REQUEST);
        });

        User user = new User(request.getLogin(), passwordEncoder.encode(PasswordValidator.decodeAndValidatePassword(request.getPassword())));

        user.getEmailData().setEmail(request.getEmail());

        userRepository.save(user);

        return user;
    }

    @Override
    public User signIn(SignInRequest request) {
        userRepository.findByLogin(request.getLogin()).ifPresent((val) -> {
            throw new ServiceException("User with given login already exists", HttpStatus.BAD_REQUEST);
        });

        userRepository.findByEmailDataEmail(request.getEmail()).ifPresent((val) -> {
            throw new ServiceException("User with given login already exists", HttpStatus.BAD_REQUEST);
        });

        User user = new User(request.getLogin(), passwordEncoder.encode(PasswordValidator.decodeAndValidatePassword(request.getPassword())));

        user.getEmailData().setEmail(request.getEmail());

        userRepository.save(user);

        log.info("Registered new user with login: " + user.getLogin() + " and ID " + user.getId());

        return user;
    }

    @Override
    public BaseAuthResponse refreshToken(RefreshTokenRequest request) {
        refreshJwtProvider.validateToken(request.getRefreshToken());

        Claims tokenData = refreshJwtProvider.getDataFromToken(request.getRefreshToken());

         User user = userRepository.findById(UUID.fromString(tokenData.get("id", String.class))).orElseThrow(() -> {
            throw new ServiceException("Token invalid", HttpStatus.BAD_REQUEST);
         });

        return new BaseAuthResponse(jwtProvider.generateToken(user.getLogin(), user.getId()), refreshJwtProvider.generateToken(user.getLogin(), user.getId()), user);
    }

    @Override
    public BaseAuthResponse baseAuth(BaseAuthRequest request) {
        User user = userRepository.findByLogin(request.getLogin()).orElseThrow(() -> {
            throw new ServiceException("Check auth data", HttpStatus.BAD_REQUEST);
        });

        if(!passwordEncoder.matches(PasswordValidator.decodePassword(request.getPassword()), user.getPassword()))
            throw new ServiceException("Check auth data", HttpStatus.BAD_REQUEST);

        return new BaseAuthResponse(jwtProvider.generateToken(user.getLogin(), user.getId()), refreshJwtProvider.generateToken(user.getLogin(), user.getId()), user);
    }
}
