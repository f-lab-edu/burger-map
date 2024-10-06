package burgermap.repository;

import burgermap.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataJpaStoreRepository extends JpaRepository<Store, Long> {

    @Query("select s from Store s where s.member.memberId = :memberId")
    List<Store> findByMemberId(@Param("memberId") Long memberId);
}
