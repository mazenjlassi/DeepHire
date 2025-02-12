package org.example.deephire.Service;

import org.example.deephire.Repositories.CertificationRepository;
import org.example.deephire.models.Certification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificationService {
    @Autowired
    private CertificationRepository certificationRepository;

    public Certification add(Certification certification) {return certificationRepository.save(certification);}

    public Certification find (Long id) {return certificationRepository.findById(id).get();}

    public void delete (Long id){certificationRepository.deleteById(id);}

    public Certification update (Certification certification){
        return certificationRepository.saveAndFlush(certification);}
}
