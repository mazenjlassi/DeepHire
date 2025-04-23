package com.deephire.Request;



import com.deephire.Models.Certification;
import com.deephire.Models.Education;
import com.deephire.Models.Experience;
import com.deephire.Models.Profile.*;
import com.deephire.Models.Skill;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ProfileCompletionRequest {

    @Size(max = 500)
    private String bio;

    @Size(max = 100)
    private String location;

    @Size(max = 120)
    private String headline;

    @Size(max = 2000)
    private String summary;

    private List<Skill> skills;
    private List<Experience> experiences;
    private List<Education> education;
    private List<Certification> certifications;

    // Getters and Setters
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

    public List<Education> getEducation() {
        return education;
    }

    public void setEducation(List<Education> education) {
        this.education = education;
    }

    public List<Certification> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications;
    }

    private MultipartFile profilePicture;
    private MultipartFile backGroundImage;

    // Getters and Setters (existing ones remain the same)

    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(MultipartFile profilePicture) {
        this.profilePicture = profilePicture;
    }

    public MultipartFile getBackGroundImage() {
        return backGroundImage;
    }

    public void setBackGroundImage(MultipartFile backGroundImage) {
        this.backGroundImage = backGroundImage;
    }

}
