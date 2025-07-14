package com.fptu.hk7.programservice.InterFace;

import com.fptu.hk7.programservice.dto.Request.MajorRequest;
import com.fptu.hk7.programservice.dto.Response.DataResponse;
import com.fptu.hk7.programservice.dto.Response.MajorResponse;
import com.fptu.hk7.programservice.pojo.Major;

import java.util.Optional;
import java.util.UUID;

public interface IMajorService {
    Major createMajor(MajorRequest majorRequest);
    DataResponse<MajorResponse> getAllMajors(int page, int size);
    Optional<Major> getMajorById(UUID id);
    Major updateMajor(UUID id,MajorRequest major);
    void deleteMajor(UUID id);
}
