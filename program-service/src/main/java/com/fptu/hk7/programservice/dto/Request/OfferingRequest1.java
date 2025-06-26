package com.fptu.hk7.programservice.dto.Request;

import com.fptu.hk7.programservice.pojo.Campus;
import com.fptu.hk7.programservice.pojo.Specialization;
import lombok.Data;

@Data
public class OfferingRequest1 {
    private int year;
    private int target;
    private long price;
    private Campus campus;
    private Specialization specialization;
}
