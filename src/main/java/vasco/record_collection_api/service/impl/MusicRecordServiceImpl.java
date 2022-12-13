package vasco.record_collection_api.service.impl;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vasco.record_collection_api.exceptions.BusinessRuleException;
import vasco.record_collection_api.model.entity.MusicRecord;
import vasco.record_collection_api.model.repository.MusicRecordRepository;
import vasco.record_collection_api.service.MusicRecordService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
public class MusicRecordServiceImpl implements MusicRecordService {

    private MusicRecordRepository repository;

    public MusicRecordServiceImpl(MusicRecordRepository repository) {
        super();
        this.repository = repository;
    }

    @Override
    @Transactional
    public MusicRecord save(MusicRecord musicRecord) {
        validate(musicRecord);
        return repository.save(musicRecord);
    }

    @Override
    @Transactional
    public MusicRecord update(MusicRecord musicRecord) {
        Objects.requireNonNull(musicRecord.getId());
        validate(musicRecord);
        return repository.save(musicRecord);
    }

    @Override
    @Transactional
    public void delete(MusicRecord musicRecord) {
        Objects.requireNonNull(musicRecord.getId());
        repository.delete(musicRecord);
    }

    @Override
    public void validate(MusicRecord musicRecord) {
        if(musicRecord.getName() == null || musicRecord.getName().trim().equals("")){
            throw new BusinessRuleException("The name of the record is mandatory!");
        }
        if(musicRecord.getArtist() == null || musicRecord.getArtist().trim().equals("")){
            throw new BusinessRuleException("Artist is mandatory!");
        }
        if(musicRecord.getLabel() == null || musicRecord.getLabel().trim().equals("")){
            throw new BusinessRuleException("Record Label is mandatory!");
        }
        if(musicRecord.getYear() == null || musicRecord.getYear().toString().length() != 4){
            throw new BusinessRuleException("A valid year is mandatory!");
        }
        if(musicRecord.getPictureUrl() == null || musicRecord.getPictureUrl().trim().equals("")){
            throw new BusinessRuleException("The url of a picture is mandatory!");
        }
        if(musicRecord.getSpotifyUrl() == null || musicRecord.getSpotifyUrl().trim().equals("")){
            throw new BusinessRuleException("The url of spotify or youtube is mandatory!");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MusicRecord> get(MusicRecord musicRecord) {
        Example example = Example.of(musicRecord,
                ExampleMatcher.matching().withIgnoreCase()
                              .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return repository.findAll(example);
    }

    @Override
    public Optional<MusicRecord> getById(Long id) {
        return repository.findById(id);
    }
}
