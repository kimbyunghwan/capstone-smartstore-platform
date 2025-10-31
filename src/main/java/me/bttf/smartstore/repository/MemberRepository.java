package me.bttf.smartstore.repository;

import me.bttf.smartstore.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
