package com.deephire.Service;

import com.deephire.Repositories.StatusOfCvRepository;
import com.deephire.models.StatusOfCv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusOfCvService {
    @Autowired
    private StatusOfCvRepository statusOfCvRepository;

    public StatusOfCv add(StatusOfCv statusOfCv) { return statusOfCvRepository.save(statusOfCv); }

    public StatusOfCv find(Long id) { return statusOfCvRepository.findById(id).get(); }

    public void delete(Long id) { statusOfCvRepository.deleteById(id); }

    public StatusOfCv update(StatusOfCv statusOfCv) { return statusOfCvRepository.saveAndFlush(statusOfCv); }

    public List<StatusOfCv> findAll() { return statusOfCvRepository.findAll(); }
}
