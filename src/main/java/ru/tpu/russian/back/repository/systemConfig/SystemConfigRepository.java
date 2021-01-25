package ru.tpu.russian.back.repository.systemConfig;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.SystemParameter;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemParameter, String>, ISystemConfigRepository {

}
