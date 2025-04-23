package com.deephire.Service;

import com.deephire.Repositories.MediaRepository;
import com.deephire.Models.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MediaService {
    @Autowired
    private MediaRepository mediaRepository;

    public Media add(Media media) {
        return mediaRepository.save(media);
    }

    public Media find(Long id) {
        return mediaRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        mediaRepository.deleteById(id);
    }

    public Media update(Media media) {
        return mediaRepository.saveAndFlush(media);
    }

    public List<Media> findAll() {
        return mediaRepository.findAll();
    }
}