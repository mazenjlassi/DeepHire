    package com.deephire.Models;

    import jakarta.persistence.*;
    import lombok.*;

    import java.util.List;
    import java.util.Set;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Entity
    public class AdminCompany extends User {

        private Boolean isValid;

        @Lob
        @Column(columnDefinition = "LONGBLOB")
        private byte[]   file;

        @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL)
        private Company company;

        public void manageRHUser(RHCompany rhUser) {
            // Business logic
        }

        public AdminCompany(String username, String email, String password, String firstName, String lastName, Boolean isValid, byte[] file) {
            super(username, email, password, firstName, lastName);
            this.isValid = isValid;
            this.file = file;
        }


    }