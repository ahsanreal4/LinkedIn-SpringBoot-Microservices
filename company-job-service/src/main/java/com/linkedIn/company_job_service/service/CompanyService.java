package com.linkedIn.company_job_service.service;

import com.linkedIn.company_job_service.dto.company.*;
import com.linkedIn.company_job_service.dto.company.locations.CreateCompanyLocationsDto;
import com.linkedIn.company_job_service.dto.company.locations.UpdateCompanyLocationDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CompanyService {
    void createCompany(CreateCompanyDto createCompanyDto, long userId);
    void addCompanyLocations(CreateCompanyLocationsDto createCompanyLocationsDto);
    String uploadCompanyFile(MultipartFile file, long companyId, String fileType);
    void updateCompany(UpdateCompanyDto updateCompanyDto, long companyId);
    void updateCompanyLocation(UpdateCompanyLocationDto updateCompanyLocationDto, long companyLocationId);
    CompanyDto getCompanyById(long companyId);
    List<CompanyDto> getAllCompanies();
    DetailedCompanyDto getCompanyDetailedData(long companyId);
    void deleteCompanyById(long companyId, long userId, boolean isAdmin);
    void deleteCompanyLocation(long companyLocationId);
}
