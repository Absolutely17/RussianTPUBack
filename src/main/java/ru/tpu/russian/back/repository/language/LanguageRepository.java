package ru.tpu.russian.back.repository.language;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.language.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String>, ILanguageRepository {

    Language getById(String id);
}
