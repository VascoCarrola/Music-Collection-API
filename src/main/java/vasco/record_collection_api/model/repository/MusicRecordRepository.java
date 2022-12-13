package vasco.record_collection_api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vasco.record_collection_api.model.entity.MusicRecord;

public interface MusicRecordRepository extends JpaRepository<MusicRecord, Long> {

}
