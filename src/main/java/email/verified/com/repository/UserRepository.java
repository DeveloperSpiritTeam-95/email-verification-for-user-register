package email.verified.com.repository;

import email.verified.com.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<Users, Long> {

    
    @Query("SELECT u FROM Users u WHERE u.email = ?1")
    Users findByEmail(String email);

    @Query("SELECT u FROM Users u WHERE u.verificationCode = ?1")
    Users findByVerificationCode(String code);
}
