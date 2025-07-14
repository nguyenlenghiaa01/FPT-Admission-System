package com.fptu.hk7.candidateservice.InterFace;

import com.fptu.hk7.candidateservice.pojo.Subject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ISubjectService {
    Subject createSubject(Subject subject);
    Optional<Subject> getSubjectById(UUID id);
    List<Subject> getAllSubjects();
    boolean deleteSubject(UUID id);
}
