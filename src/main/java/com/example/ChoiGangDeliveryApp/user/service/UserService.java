package com.example.ChoiGangDeliveryApp.user.service;

import com.example.ChoiGangDeliveryApp.common.exception.GlobalErrorCode;
import com.example.ChoiGangDeliveryApp.common.exception.GlobalException;
import com.example.ChoiGangDeliveryApp.common.file.FileService;
import com.example.ChoiGangDeliveryApp.common.util.GlobalConstants;
import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.enums.VerificationStatus;
import com.example.ChoiGangDeliveryApp.jwt.JwtTokenUtils;
import com.example.ChoiGangDeliveryApp.jwt.dto.JwtRequestDto;
import com.example.ChoiGangDeliveryApp.jwt.dto.JwtResponseDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantDto;
import com.example.ChoiGangDeliveryApp.security.config.AuthenticationFacade;
import com.example.ChoiGangDeliveryApp.security.config.CustomUserDetails;
import com.example.ChoiGangDeliveryApp.user.dto.PasswordChangeRequestDto;
import com.example.ChoiGangDeliveryApp.user.dto.PasswordDto;
import com.example.ChoiGangDeliveryApp.user.dto.UserCreateDto;
import com.example.ChoiGangDeliveryApp.user.dto.UserDto;
import com.example.ChoiGangDeliveryApp.user.entity.DriverRoleRequest;
import com.example.ChoiGangDeliveryApp.user.entity.EmailVerification;
import com.example.ChoiGangDeliveryApp.user.entity.OwnerRoleRequest;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import com.example.ChoiGangDeliveryApp.user.repo.DriverRoleRequestRepository;
import com.example.ChoiGangDeliveryApp.user.repo.OwnerRoleRequestRepository;
import com.example.ChoiGangDeliveryApp.user.repo.UserRepository;
import com.example.ChoiGangDeliveryApp.user.repo.VerificationRepository;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.model.jdbc.UpsertOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationFacade facade;
    private final VerificationRepository verificationRepository;
    private final JavaMailSender javaMailSender;
    private final FileService fileService;
    private final OwnerRoleRequestRepository ownerRoleRequestRepository;
    private final DriverRoleRequestRepository driverRoleRequestRepository;


    //First of all, user register
    // User must be fill registerDto (username, pass, confirm-pass, email, address) to register
    // Then create a new user with status is
    //sign up

    // Confirm the ID registered with the email address
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
    @Transactional
    public UserDto createUser(UserCreateDto dto){
        //check if email is existing
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new GlobalException(GlobalErrorCode.EMAIL_ALREADY_EXISTS);

        // Check if email is in Verification Email table
        EmailVerification verification = verificationRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.VERIFICATION_NOT_FOUND));

        //Check verification status.
        if (!verification.getStatus().equals(VerificationStatus.VERIFIED)) {
            throw new GlobalException(GlobalErrorCode.VERIFICATION_INVALID_STATUS);
        }

        //Check pass
        if (!dto.getPassword().equals(dto.getPassCheck()))
            throw new GlobalException(GlobalErrorCode.PASSWORD_MISMATCH);
        // Check username
        if (userRepository.existsByUsername(dto.getUsername()))
            throw new GlobalException(GlobalErrorCode.USERNAME_ALREADY_EXISTS);

        //Create a new user
        UserEntity newUser = UserEntity.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(UserRole.ROLE_USER)
                .build();
        // Once the user is created, the email will be removed from the database (as they are no longer needed).
        verificationRepository.deleteByEmail(dto.getEmail());

        return UserDto.fromEntity(userRepository.save(newUser));
    }


    //log in
    // Generate json web token (jwt)
    public JwtResponseDto login(JwtRequestDto dto){
        UserEntity userEntity = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(
                dto.getPassword(),
                userEntity.getPassword()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        String jwt = jwtTokenUtils.generateToken(CustomUserDetails.fromEntity(userEntity));
        JwtResponseDto response = new JwtResponseDto();
        response.setToken(jwt);
        return response;
    }

    //Send verify code
    @Value("${SMTP_USERNAME}")
    private String senderEmail;

    @Transactional
    public void sendVerifyCode(String receiverEmail) {
        String verifyCode = generateRandomNumber(6);

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setFrom(senderEmail);
            message.setRecipients(Message.RecipientType.TO, receiverEmail);

            message.setSubject("[ChoiGangDelivery] 인증 코드입니다.");

            StringBuilder textBody = new StringBuilder();
            textBody.append("<div style=\"max-width: 600px;\">")
                    .append("   <h2>Email Verification</h2>")
                    .append("   <p>아래의 인증 코드를 사용하여 이메일 주소를 인증해주세요.</p>")
                    .append("   <p><strong>인증 코드:</strong> <span style=\"font-size: 18px; font-weight: bold;\">")
                    .append(verifyCode)
                    .append("</span></p>")
                    .append("   <p>이 코드는 5분간 유효합니다.</p>")
                    .append("   <p>감사합니다.</p>")
                    .append("</div>");
            message.setText(textBody.toString(), "UTF-8", "HTML");

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new GlobalException(GlobalErrorCode.EMAIL_SENDING_FAILED);
        }

        // Delete the existing verify code sending history
        verificationRepository.deleteByEmail(receiverEmail);

        EmailVerification verification = EmailVerification.builder()
                .email(receiverEmail)
                .verifyCode(verifyCode)
                .status(VerificationStatus.SENT)
                .build();

        // Save authentication code sending history
        verificationRepository.save(verification);
    }

    public void verifyEmail(String email, String code) {
        EmailVerification verification = verificationRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.VERIFICATION_NOT_FOUND));

        if (!verification.getVerifyCode().equals(code)) {
            // Check if the verify code sent to user email matches the verify code stored in the DB.
            throw new GlobalException(GlobalErrorCode.VERIFICATION_CODE_MISMATCH);
        } else if (!verification.getStatus().equals(VerificationStatus.SENT)) {
            // If verify code is not appropriate
            throw new GlobalException(GlobalErrorCode.VERIFICATION_INVALID_STATUS);
        } else if (verification.getCreatedAt().isBefore(
                LocalDateTime.now().minusMinutes(GlobalConstants.EMAIL_VERIFY_CODE_EXPIRE_SECOND))) {
            // Verification time expired
            throw new GlobalException(GlobalErrorCode.VERIFICATION_EXPIRED);
        }
        verification.setStatus(VerificationStatus.VERIFIED);
        verificationRepository.save(verification);
        UserEntity user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.EMAIL_NOT_FOUND));
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    // Method to generate random number code
    public String generateRandomNumber(int len) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    //Send verification code when registering
    @Transactional
    public ResponseEntity<String> signUpSendCode(String email) {
        if (userExists(email))
            throw new GlobalException(GlobalErrorCode.EMAIL_ALREADY_EXISTS);
        sendVerifyCode(email);
        return ResponseEntity.ok("{}");
    }
    //This method verifies the code
    // and updates the user's password if the verification code is valid.
    @Transactional
    public ResponseEntity<String> passwordSendCode(String email) {
        //Check if any user has this email
        if (!userExists(email))
            throw new GlobalException(GlobalErrorCode.EMAIL_NOT_FOUND);
        sendVerifyCode(email);
        return ResponseEntity.ok("{}");
    }
    //reset password
    @Transactional
    public ResponseEntity<String> resetPassword(PasswordChangeRequestDto requestDto) {
        String email = requestDto.getEmail();
        String code = requestDto.getCode();
        String newPassword = requestDto.getNewPassword();

        EmailVerification verification = verificationRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.VERIFICATION_NOT_FOUND));

        if (!verification.getVerifyCode().equals(code)) {
            // Check if verify code sent to user's email matches the verify code stored in the DB.
            throw new GlobalException(GlobalErrorCode.VERIFICATION_CODE_MISMATCH);
        } else if (!verification.getStatus().equals(VerificationStatus.VERIFIED)) {
            // If the verify code is not appropriate
            throw new GlobalException(GlobalErrorCode.VERIFICATION_INVALID_STATUS);
        }

        UserEntity user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.EMAIL_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        verificationRepository.deleteByEmail(email);

        return ResponseEntity.ok("Success");
    }
    // Change password method
    @Transactional
    public ResponseEntity<String> changePassword(PasswordDto dto) {
        UserEntity currentUser= facade.getCurrentUserEntity();

        //Check if password matches
        if (!passwordEncoder.matches(dto.getCurrentPassword(), currentUser.getPassword())) {
            throw new GlobalException(GlobalErrorCode.PASSWORD_MISMATCH);
        }

        currentUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(currentUser);

        return ResponseEntity.ok("{}");
    }
    public void uploadProfileImage(MultipartFile image) throws Exception {
        UserEntity currentUser= facade.getCurrentUserEntity();
        //check if user is existing
        Optional<UserEntity> userOpt = userRepository.findUserById(currentUser.getId());
        if (userOpt.isEmpty()) {
            throw new GlobalException(GlobalErrorCode.USER_MISMATCH);
        }

        //Determine the upload directory for the profile image
        String userImgDir = "media/imgProfiles/" + currentUser.getId() + "/"; // media/imgProfile/{UserId}

        // Check and create the profile image folder if it doesn't exist
        try {
            Files.createDirectories(Path.of(userImgDir));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating profile directory");
        }

        // Delete existing profile image

        String oldProfile = currentUser.getProfileImgPath();
        if (oldProfile != null) {
            try {
                fileService.deleteFile(userImgDir + oldProfile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // Save img by FileService and get saved name
        String savedFileName = fileService.uploadFile(userImgDir, image.getOriginalFilename(), image.getBytes());

        // 6.  set RestImage
        String reqPath = "/static/imgProfiles/" + currentUser.getId() + "/" + savedFileName;
        currentUser.setProfileImgPath(reqPath);

        // 7. save and return RestaurantDto
        userRepository.save(currentUser);

    }

    //View Profile
    public UserDto getMyProfile() {
        UserEntity user = facade.getCurrentUserEntity();
        return UserDto.fromEntity(user);
    }
    // After user register and email verified, can request to upgrade role.
    //Request Owner role
    public void requestOwnerRole(String businessNumber) {
        UserEntity currentUser = facade.getCurrentUserEntity();
        //check if user is existing
        Optional<UserEntity> userOpt = userRepository.findUserById(currentUser.getId());
        if (userOpt.isEmpty()) {
            throw new GlobalException(GlobalErrorCode.USER_MISMATCH);
        }
        if (!currentUser.isEmailVerified()) {
            throw new GlobalException(GlobalErrorCode.EMAIL_NOT_VERIFIED);
        }
        if (currentUser.getRole() != UserRole.ROLE_USER) {
            throw new GlobalException(GlobalErrorCode.INVALID_ROLE_FOR_REQUEST);
        }

        // Check if the user has requested to switch role.
        Optional<OwnerRoleRequest> optionalRequest = ownerRoleRequestRepository.findByUserAndStatus(currentUser, ApprovalStatus.PENDING);

        if (optionalRequest.isPresent())
            throw new GlobalException(GlobalErrorCode.DUPLICATE_MANAGER_REQUEST);

        // New request
        OwnerRoleRequest request = OwnerRoleRequest.builder()
                .user(currentUser)
                .businessNumber(businessNumber)
                .status(ApprovalStatus.PENDING)
                .build();
        ownerRoleRequestRepository.save(request);
    }

    // Request Driver role
    public void requestDriverRole(String licenseNumber) {
        UserEntity currentUser = facade.getCurrentUserEntity();

        Optional<UserEntity> userOpt = userRepository.findUserById(currentUser.getId());
        if (userOpt.isEmpty()) {
            throw new GlobalException(GlobalErrorCode.USER_MISMATCH);
        }

        if (!currentUser.isEmailVerified()) {
            throw new GlobalException(GlobalErrorCode.EMAIL_NOT_VERIFIED);
        }

        if (currentUser.getRole() != UserRole.ROLE_USER) {
            throw new GlobalException(GlobalErrorCode.INVALID_ROLE_FOR_REQUEST);
        }

        Optional<DriverRoleRequest> optionalRequest = driverRoleRequestRepository.findByUserAndStatus(currentUser, ApprovalStatus.PENDING);

        if (optionalRequest.isPresent()) {
            throw new GlobalException(GlobalErrorCode.DUPLICATE_DRIVER_REQUEST);
        }

        DriverRoleRequest request = DriverRoleRequest.builder()
                .user(currentUser)
                .licenseNumber(licenseNumber)
                .status(ApprovalStatus.PENDING)
                .build();
        driverRoleRequestRepository.save(request);
    }






}