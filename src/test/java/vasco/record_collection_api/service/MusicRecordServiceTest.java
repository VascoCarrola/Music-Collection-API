package vasco.record_collection_api.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import vasco.record_collection_api.exceptions.BusinessRuleException;
import vasco.record_collection_api.model.entity.MusicRecord;
import vasco.record_collection_api.model.repository.MusicRecordRepository;
import vasco.record_collection_api.model.repository.MusicRecordRepositoryTest;
import vasco.record_collection_api.service.impl.MusicRecordServiceImpl;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class MusicRecordServiceTest {
    @SpyBean
    MusicRecordServiceImpl service;

    @MockBean
    MusicRecordRepository repository;

    @Test
    public void shouldSaveMusicRecords(){
        MusicRecord musicRecordToSave = MusicRecordRepositoryTest.createRecord();
        Mockito.doNothing().when(service).validate(musicRecordToSave);

        MusicRecord savedMusicRecord = MusicRecordRepositoryTest.createRecord();
        savedMusicRecord.setId(1l);
        Mockito.when(repository.save(musicRecordToSave)).thenReturn(savedMusicRecord);

        MusicRecord musicRecord = service.save(musicRecordToSave);
        Assertions.assertThat(musicRecord.getId()).isEqualTo(savedMusicRecord.getId());
    }

    @Test
    public void shouldNotSaveMusicRecordsWhenValidationThrowsError(){
        MusicRecord musicRecordToSave = MusicRecordRepositoryTest.createRecord();
        Mockito.doThrow(BusinessRuleException.class).when(service).validate(musicRecordToSave);

        Assertions.catchThrowableOfType(() -> service.save(musicRecordToSave), BusinessRuleException.class);

        Mockito.verify(repository, Mockito.never()).save(musicRecordToSave);

    }

    @Test
    public void shouldUpdateMusicRecords(){
        MusicRecord savedMusicRecord = MusicRecordRepositoryTest.createRecord();
        savedMusicRecord.setId(1l);

        Mockito.doNothing().when(service).validate(savedMusicRecord);
        Mockito.when(repository.save(savedMusicRecord)).thenReturn(savedMusicRecord);

        MusicRecord musicRecord = service.update(savedMusicRecord);

        Mockito.verify(repository, Mockito.times(1)).save(savedMusicRecord);
    }

    @Test
    public void shouldNotUpdateMusicRecordThatDoNotExist(){
        MusicRecord notSavedMusicRecord = MusicRecordRepositoryTest.createRecord();
        Assertions.catchThrowableOfType(() -> service.update(notSavedMusicRecord), NullPointerException.class);
        Mockito.verify(repository, Mockito.never()).save(notSavedMusicRecord);
    }

    @Test
    public void shouldDeleteMusicRecordSavedOnDataBase(){
        MusicRecord savedMusicRecord = MusicRecordRepositoryTest.createRecord();
        savedMusicRecord.setId(1l);

        service.delete(savedMusicRecord);
        Mockito.verify(repository).delete(savedMusicRecord);
    }

    @Test
    public void shouldNotDeleteMusicRecordThatDoNotExistInDataBase(){
        MusicRecord notSavedMusicRecord = MusicRecordRepositoryTest.createRecord();
        Assertions.catchThrowableOfType(() -> service.delete(notSavedMusicRecord), NullPointerException.class);
        Mockito.verify(repository, Mockito.never()).delete(notSavedMusicRecord);
    }

    @Test
    public void shouldGetMusicRecordById(){
        Long id = 1l;
        MusicRecord musicRecord = MusicRecordRepositoryTest.createRecord();
        musicRecord.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(musicRecord));

        Optional<MusicRecord> returnedRecord = service.getById(id);
        Assertions.assertThat(returnedRecord.isPresent()).isTrue();
    }

    @Test
    public void shouldReturnEmptyOptionalIfMusicRecordNotExists(){
       Long id = 1l;

       Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

       Optional<MusicRecord> returnedRecord = service.getById(id);
       Assertions.assertThat(returnedRecord.isPresent()).isFalse();
    }

    @Test
    public void shouldThrowExceptionTryingToValidateIncompleteMusicRecord(){
        MusicRecord musicRecord = new MusicRecord();

        Throwable exception = Assertions.catchThrowable(() -> service.validate(musicRecord));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("The name of the record is mandatory!");

        musicRecord.setName("");
        exception = Assertions.catchThrowable(() -> service.validate(musicRecord));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("The name of the record is mandatory!");

        musicRecord.setName("name");
        exception = Assertions.catchThrowable(() -> service.validate(musicRecord));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Artist is mandatory!");

        musicRecord.setArtist("");
        exception = Assertions.catchThrowable(() -> service.validate(musicRecord));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Artist is mandatory!");

        musicRecord.setArtist("artist");
        exception = Assertions.catchThrowable(() -> service.validate(musicRecord));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Record Label is mandatory!");

        musicRecord.setLabel("");
        exception = Assertions.catchThrowable(() -> service.validate(musicRecord));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("Record Label is mandatory!");

        musicRecord.setLabel("label");
        exception = Assertions.catchThrowable(() -> service.validate(musicRecord));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("A valid year is mandatory!");

        musicRecord.setYear(123);
        exception = Assertions.catchThrowable(() -> service.validate(musicRecord));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("A valid year is mandatory!");

        musicRecord.setYear(1234);
        exception = Assertions.catchThrowable(() -> service.validate(musicRecord));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("The url of a picture is mandatory!");

        musicRecord.setPictureUrl("");
        exception = Assertions.catchThrowable(() -> service.validate(musicRecord));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("The url of a picture is mandatory!");

        musicRecord.setPictureUrl("pictureUrl");
        exception = Assertions.catchThrowable(() -> service.validate(musicRecord));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("The url of spotify or youtube is mandatory!");

        musicRecord.setSpotifyUrl("");
        exception = Assertions.catchThrowable(() -> service.validate(musicRecord));
        Assertions.assertThat(exception).isInstanceOf(BusinessRuleException.class).hasMessage("The url of spotify or youtube is mandatory!");
    }
}
