package com.example.aiwriter.config;

import com.example.aiwriter.entity.User;
import com.example.aiwriter.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
public class DefaultUserInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DefaultUserInitializer.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${app.default-user.enabled:false}")
    private boolean defaultUserEnabled;

    @Value("${app.default-user.username:admin}")
    private String defaultUsername;

    @Value("${app.default-user.password:admin123}")
    private String defaultPassword;

    @Value("${app.default-user.email:admin@example.com}")
    private String defaultEmail;

    @Value("${app.default-user.daily-quota:100}")
    private Integer defaultDailyQuota;

    public DefaultUserInitializer(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!defaultUserEnabled) {
            log.info("默认用户初始化已禁用 (app.default-user.enabled=false)");
            return;
        }

        if (userRepository.existsByUsername(defaultUsername)) {
            log.info("默认用户 '{}' 已存在，跳过初始化", defaultUsername);
            return;
        }

        try {
            User user = new User();
            user.setUsername(defaultUsername);
            user.setPassword(passwordEncoder.encode(defaultPassword));
            user.setEmail(defaultEmail);
            user.setDailyQuota(defaultDailyQuota);
            user.setUsedQuota(0);

            userRepository.save(user);

            log.warn("=============================================");
            log.warn("默认用户已创建成功!");
            log.warn("用户名: {}", defaultUsername);
            log.warn("密码: {}", defaultPassword);
            log.warn("邮箱: {}", defaultEmail);
            log.warn("每日配额: {}", defaultDailyQuota);
            log.warn("=============================================");
            log.warn("⚠️  请在生产环境中修改默认密码!");
            log.warn("=============================================");

        } catch (Exception e) {
            log.error("创建默认用户失败: {}", e.getMessage());
        }
    }
}
