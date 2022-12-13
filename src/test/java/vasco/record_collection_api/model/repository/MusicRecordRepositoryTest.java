package vasco.record_collection_api.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import vasco.record_collection_api.model.entity.MusicRecord;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MusicRecordRepositoryTest {

    @Autowired
    MusicRecordRepository repository;

    @Autowired
    TestEntityManager entityManager;

    public static MusicRecord createRecord(){
        return MusicRecord.builder().artist("artist").name("name").label("label").year(2022).pictureUrl("url").spotifyUrl("url").build();
    }

    @Test
    public void shouldSaveAMusicRecord(){
        MusicRecord musicRecord = createRecord();

        MusicRecord savedRecord = repository.save(musicRecord);

        Assertions.assertThat(savedRecord.getId()).isNotNull();
    }

    @Test
    public void shouldDeleteAMusicRecord(){
        MusicRecord musicRecord = createRecord();

        entityManager.persist(musicRecord);

        repository.delete(musicRecord);

        MusicRecord deletedRecord = entityManager.find(MusicRecord.class, musicRecord.getId());
        Assertions.assertThat(deletedRecord).isNull();
    }

    @Test
    public void shouldUpdateMusicRecord(){
       MusicRecord musicRecord = createRecord();
       entityManager.persist(musicRecord);
       musicRecord.setYear(2000);
       musicRecord.setLabel("updatedLabel");
       musicRecord.setArtist("updatedArtist");
       musicRecord.setName("updatedName");
       musicRecord.setPictureUrl("updatedUrl");
       musicRecord.setSpotifyUrl("updatedUrl");

       repository.save(musicRecord);

       MusicRecord updatedMusicRecord = entityManager.find(MusicRecord.class, musicRecord.getId());
       Assertions.assertThat(updatedMusicRecord.getId()).isEqualTo(musicRecord.getId());
       Assertions.assertThat(updatedMusicRecord.getYear()).isEqualTo(2000);
       Assertions.assertThat(updatedMusicRecord.getLabel()).isEqualTo("updatedLabel");
       Assertions.assertThat(updatedMusicRecord.getArtist()).isEqualTo("updatedArtist");
       Assertions.assertThat(updatedMusicRecord.getName()).isEqualTo("updatedName");
       Assertions.assertThat(updatedMusicRecord.getPictureUrl()).isEqualTo("updatedUrl");
       Assertions.assertThat(updatedMusicRecord.getSpotifyUrl()).isEqualTo("updatedUrl");
    }

    @Test
    public void shouldGetMusicRecordById(){
        MusicRecord musicRecord = createRecord();
        entityManager.persist(musicRecord);

        Optional<MusicRecord> foundRecord = repository.findById(musicRecord.getId());

        Assertions.assertThat(foundRecord.isPresent()).isTrue();
    }
}
