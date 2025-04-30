package com.deephire.Service;

import com.deephire.Models.Profile;
import com.deephire.Repositories.CertificationRepository;
import com.deephire.Models.Certification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificationService {
    @Autowired
    private CertificationRepository certificationRepository;

    public Certification add(Certification certification) {return certificationRepository.save(certification);}

    public Certification find (Long id) {return certificationRepository.findById(id).get();}

    public void delete (Long id){certificationRepository.deleteById(id);}

    public Certification update (Certification certification){
        return certificationRepository.saveAndFlush(certification);}

    public List<Certification> findAll(){ return certificationRepository.findAll();}

    public List<Certification> saveAllCertificationsWithProfile(List<Certification> certifications, Profile profile) {
        certifications.forEach(certification -> certification.setProfile(profile));
        return certificationRepository.saveAll(certifications);
    }
    public  void  delete(Certification certification){
        certificationRepository.delete(certification);
    }

}
