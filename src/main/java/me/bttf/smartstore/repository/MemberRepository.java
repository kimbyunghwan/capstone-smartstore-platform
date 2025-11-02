package me.bttf.smartstore.repository;

import me.bttf.smartstore.domain.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);


    @Query("""
        select distinct m
        from Member m
        left join fetch m.roles
        where m.email = :email
    """)
    Optional<Member> findByEmail(@Param("email") String email);
}
