package com.nrzm.demo.config;

import com.nrzm.demo.entitiy.Member;
import com.nrzm.demo.entitiy.User;
import com.nrzm.demo.repository.MemberRepository;
import com.nrzm.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev") // 개발 프로파일에서만 실행되도록 설정 (CLI Run Config, --spring.profiles.active=dev)
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        Member user = new Member();
        user.setId(1L);
        user.setMemberName("user");
        user.setMemberPassword(passwordEncoder.encode("1234"));
        user.setRole("ROLE_USER");
        memberRepository.save(user);

        Member admin = new Member();
        admin.setId(2L);
        admin.setMemberName("admin");
        admin.setMemberPassword(passwordEncoder.encode("1234"));
        admin.setRole("ROLE_ADMIN");
        memberRepository.save(admin);

        try {
            if (mongoTemplate != null) {
                // 초기 데이터 삽입 전에 기존 데이터 삭제 (선택 사항)
                userRepository.deleteAll();

                User user1 = new User();
                user1.setName("홍길동");
                user1.setAge(16);
                user1.setStatus("active");

                User user2 = new User();
                user2.setName("이순신");
                user2.setAge(24);
                user2.setStatus("active");

                User user3 = new User();
                user3.setName("강감찬");
                user3.setAge(37);
                user3.setStatus("active");

                User user4 = new User();
                user4.setName("아무개");
                user4.setAge(11);
                user4.setStatus("inactive");

                userRepository.save(user1);
                userRepository.save(user2);
                userRepository.save(user3);
                userRepository.save(user4);
            }
        } catch (Exception e) {
            // MongoDB 연결 실패 시 Skip
        }
    }
}
