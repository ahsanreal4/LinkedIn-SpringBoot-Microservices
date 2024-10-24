package com.linkedIn.company_job_service.controller;

import com.linkedIn.company_job_service.dto.company.*;
import com.linkedIn.company_job_service.dto.company.locations.CreateCompanyLocationsDto;
import com.linkedIn.company_job_service.dto.company.locations.UpdateCompanyLocationDto;
import com.linkedIn.company_job_service.service.CompanyService;
import com.linkedIn.company_job_service.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    private final CompanyService companyService;
    private final UserUtils userUtils;

    public CompanyController(CompanyService companyService,
                             UserUtils userUtils) {
        this.companyService = companyService;
        this.userUtils = userUtils;
    }

    @PostMapping("")
    public void createCompany(@Valid @RequestBody CreateCompanyDto createCompanyDto, HttpServletRequest request) {
        long userId = userUtils.getUserId(request);

        this.companyService.createCompany(createCompanyDto, userId);
    }

    @PostMapping("/locations")
    public void addCompanyLocations(@Valid @RequestBody CreateCompanyLocationsDto createCompanyLocationsDto) {
        this.companyService.addCompanyLocations(createCompanyLocationsDto);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCompanyFile(@RequestParam("file") MultipartFile file, @RequestParam("type") String fileType,
                                                    @RequestParam("companyId") long companyId) {
        String url = this.companyService.uploadCompanyFile(file, companyId, fileType);

        return ResponseEntity.ok(url);
    }

    @PutMapping("/{id}")
    public void updateCompany(@Valid @RequestBody UpdateCompanyDto updateCompanyDto, @PathVariable("id") long id) {
        this.companyService.updateCompany(updateCompanyDto, id);
    }

    @PutMapping("/locations/{id}")
    public void updateCompanyLocation(@Valid @RequestBody UpdateCompanyLocationDto updateCompanyLocationDto, @PathVariable("id") long id) {
        this.companyService.updateCompanyLocation(updateCompanyLocationDto, id);
    }

    @GetMapping("")
    public ResponseEntity<List<CompanyDto>> getAllCompanies(HttpServletRequest request) {
        userUtils.shouldBeAdmin(request);

        List<CompanyDto> dtos = this.companyService.getAllCompanies();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable("id") long id) {
        CompanyDto dto = this.companyService.getCompanyById(id);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/detailed/{id}")
    public ResponseEntity<DetailedCompanyDto> getCompanyDetailedData(@PathVariable("id") long id) {
        DetailedCompanyDto dto = this.companyService.getCompanyDetailedData(id);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteCompanyById(@PathVariable("id") long id, HttpServletRequest request) {
        long userId = this.userUtils.getUserId(request);
        boolean isAdmin = this.userUtils.isAdmin(request);

        this.companyService.deleteCompanyById(id, userId, isAdmin);
    }

    @DeleteMapping("/locations/{id}")
    public void deleteCompanyLocationById(@PathVariable("id") long id) {
        this.companyService.deleteCompanyLocation(id);
    }
}
