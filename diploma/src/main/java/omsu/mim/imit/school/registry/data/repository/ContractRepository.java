package omsu.mim.imit.school.registry.data.repository;

import jakarta.transaction.Transactional;
import omsu.mim.imit.school.registry.data.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ContractRepository  extends JpaRepository<ContractEntity, UUID> {

    @Query("select c from contract c "
            + "where c.childId = :childId")
    ContractEntity getByChildId(@Param("childId") UUID childId);

    @Modifying
    @Transactional
    @Query("""
            update contract
            set file = :fileBytes
            where payerFullname = 'имя'
    """)
    void addFile(@Param("fileBytes") byte[] fileBytes);

}
