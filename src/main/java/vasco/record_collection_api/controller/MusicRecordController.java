package vasco.record_collection_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vasco.record_collection_api.exceptions.BusinessRuleException;
import vasco.record_collection_api.model.entity.MusicRecord;
import vasco.record_collection_api.model.entity.User;
import vasco.record_collection_api.service.MusicRecordService;
import vasco.record_collection_api.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class MusicRecordController {

    private final MusicRecordService service;

    private final UserService userService;

    @PostMapping
    public ResponseEntity save(@RequestBody MusicRecord musicRecord){
        try {
            service.save(musicRecord);
            return new ResponseEntity(musicRecord, HttpStatus.CREATED);
        }catch (BusinessRuleException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody MusicRecord musicRecord){
        return service.getById(id).map(entity ->{
            try{
                musicRecord.setId(id);
                service.update(musicRecord);
                return ResponseEntity.ok(musicRecord);
            }catch (BusinessRuleException e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet(() -> new ResponseEntity<>("Record doesn't exist in database!", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){
        return service.getById(id).map(entity ->{
               service.delete(entity);
               return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity<>("Record doesn't exist in database!", HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity getRecords(@RequestParam(value = "artist", required = false) String artist,
                                     @RequestParam(value = "label", required = false) String label,
                                     @RequestParam(value = "year", required = false) Integer year,
                                     @RequestParam("user") Long userId){
        MusicRecord filteredRecord = new MusicRecord();
        filteredRecord.setArtist(artist);
        filteredRecord.setLabel(label);
        filteredRecord.setYear(year);

        Optional<User> user = userService.getUserById(userId);
        if(!user.isPresent()){
            return ResponseEntity.badRequest().body("User don't exist");
        }else {filteredRecord.setUser(user.get());}

        List<MusicRecord> recordList = service.get(filteredRecord);
        return ResponseEntity.ok(recordList);
    }
}
