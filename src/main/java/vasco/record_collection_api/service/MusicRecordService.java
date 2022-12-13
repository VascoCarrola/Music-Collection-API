package vasco.record_collection_api.service;

import vasco.record_collection_api.model.entity.MusicRecord;

import java.util.List;
import java.util.Optional;

public interface MusicRecordService {

   MusicRecord save(MusicRecord musicRecord);

   MusicRecord update(MusicRecord musicRecord);

   void delete(MusicRecord musicRecord);

   void validate(MusicRecord musicRecord);

   List<MusicRecord> get(MusicRecord musicRecord);

   Optional<MusicRecord> getById(Long id);
}
