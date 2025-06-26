package com.fptu.hk7.programservice.service;

import com.fptu.hk7.programservice.dto.Request.MajorRequest;
import com.fptu.hk7.programservice.dto.Response.DataResponse;
import com.fptu.hk7.programservice.dto.Response.MajorResponse;
import com.fptu.hk7.programservice.pojo.Major;
import com.fptu.hk7.programservice.repository.MajorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MajorService {
    @Autowired
    private MajorRepository majorRepository;

    public Major createMajor(MajorRequest majorRequest) {
        Major major = new Major();
        major.setId(UUID.randomUUID());
        major.setName(majorRequest.getName());
        major.setDescription(majorRequest.getDescription());
        return majorRepository.save(major);
    }

    public DataResponse<MajorResponse> getAllMajors( int page,  int size) {
        Page<Major> majorPage = majorRepository.findAll(PageRequest.of(page, size));
        List<Major> majors = majorPage.getContent();
        List<MajorResponse> majorResponses = new ArrayList<>();
        for(Major major: majors) {
            MajorResponse majorResponse = new MajorResponse();
            majorResponse.setMajorId(major.getId());
            majorResponse.setName(major.getName());
            majorResponse.setDescription(major.getDescription());


            majorResponses.add(majorResponse);
        }

        DataResponse<MajorResponse> dataResponse = new DataResponse<MajorResponse>();
        dataResponse.setListData(majorResponses);
        dataResponse.setTotalElements(majorPage.getTotalElements());
        dataResponse.setPageNumber(majorPage.getNumber());
        dataResponse.setTotalPages(majorPage.getTotalPages());
        return dataResponse;
    }

    public Optional<Major> getMajorById(UUID id) {
        return majorRepository.findById(id);
    }

    public Major updateMajor(Major major) {
        return majorRepository.save(major);
    }

    public void deleteMajor(UUID id) {
        majorRepository.deleteById(id);
    }
}
