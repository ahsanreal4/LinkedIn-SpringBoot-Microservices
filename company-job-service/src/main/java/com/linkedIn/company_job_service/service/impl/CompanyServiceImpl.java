package com.linkedIn.company_job_service.service.impl;

import com.linkedIn.company_job_service.constants.EmployeeRanges;
import com.linkedIn.company_job_service.dto.BatchFileDeleteDto;
import com.linkedIn.company_job_service.dto.company.*;
import com.linkedIn.company_job_service.dto.company.locations.CompanyLocationDto;
import com.linkedIn.company_job_service.dto.company.locations.CreateCompanyLocationDto;
import com.linkedIn.company_job_service.dto.company.locations.CreateCompanyLocationsDto;
import com.linkedIn.company_job_service.dto.company.locations.UpdateCompanyLocationDto;
import com.linkedIn.company_job_service.entity.company.Company;
import com.linkedIn.company_job_service.entity.company.CompanyFiles;
import com.linkedIn.company_job_service.entity.company.CompanyLocations;
import com.linkedIn.company_job_service.entity.user.User;
import com.linkedIn.company_job_service.enums.CompanyFileType;
import com.linkedIn.company_job_service.exception.ApiException;
import com.linkedIn.company_job_service.feign_clients.impl.FileServiceClientImpl;
import com.linkedIn.company_job_service.repository.company.CompanyFilesRepository;
import com.linkedIn.company_job_service.repository.company.CompanyLocationsRepository;
import com.linkedIn.company_job_service.repository.company.CompanyRepository;
import com.linkedIn.company_job_service.service.CompanyService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyLocationsRepository companyLocationsRepository;
    private final CompanyFilesRepository companyFilesRepository;
    private final FileServiceClientImpl fileService;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyLocationsRepository companyLocationsRepository,
                                FileServiceClientImpl fileService, CompanyFilesRepository companyFilesRepository) {
        this.companyRepository = companyRepository;
        this.companyLocationsRepository = companyLocationsRepository;
        this.fileService = fileService;
        this.companyFilesRepository = companyFilesRepository;
    }


    @Override
    public void createCompany(CreateCompanyDto createCompanyDto, long userId) {
        if (!EmployeeRanges.isValidEmployeeRange(createCompanyDto.getNumEmployees())) throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid employee range. " +
                EmployeeRanges.LOW + ", " + EmployeeRanges.MEDIUM + ", " + EmployeeRanges.HIGH + ", " + EmployeeRanges.ENTERPRISE + " are the valid employee ranges");


        User user = new User();
        user.setId(userId);

        Company company = new Company();
        company.setName(createCompanyDto.getName());
        company.setNumEmployees(createCompanyDto.getNumEmployees());
        company.setCreatedBy(user);
        if(createCompanyDto.getAbout() != null) company.setAbout(createCompanyDto.getAbout());
        if(createCompanyDto.getWebsite() != null) company.setAbout(createCompanyDto.getWebsite());
        if(createCompanyDto.getHeadLine() != null) company.setAbout(createCompanyDto.getHeadLine());

        try {
            companyRepository.save(company);
        }
        catch (DataIntegrityViolationException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Company with this name already exists");
        }
    }

    @Override
    public void addCompanyLocations(CreateCompanyLocationsDto createCompanyLocationsDto) {
        Company company = returnCompanyOrThrowError(createCompanyLocationsDto.getCompanyId());

        CreateCompanyLocationDto[] createList = createCompanyLocationsDto.getLocations();

        List<CompanyLocations> companyLocationsList = new ArrayList<>();

        for (CreateCompanyLocationDto dto : createList) {
            CompanyLocations companyLocation = new CompanyLocations();
            companyLocation.setCity(dto.getCity());
            companyLocation.setCountry(dto.getCountry());
            companyLocation.setCompany(company);

            if(dto.getAddress() != null) companyLocation.setAddress(dto.getAddress());
            if(dto.getZipCode() != null) companyLocation.setZipCode(dto.getZipCode());

            companyLocationsList.add(companyLocation);
        }

        companyLocationsRepository.saveAll(companyLocationsList);
    }

    private CompanyDto getCompanyData(Company company) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(company.getId());
        companyDto.setAbout(company.getAbout());
        companyDto.setName(company.getName());
        companyDto.setWebsite(company.getWebsite());
        companyDto.setHeadLine(company.getHeadLine());
        companyDto.setNumEmployees(company.getNumEmployees());

        User user = company.getCreatedBy();
        companyDto.setCreatedBy(user.getId());

        Set<CompanyFiles> companyFilesSet = company.getFiles();
        if (companyFilesSet == null || companyFilesSet.isEmpty()) return companyDto;

        List<CompanyFiles> companyFiles = companyFilesSet.stream().toList();

        for(CompanyFiles file: companyFiles) {
            if (file.getType().equals(CompanyFileType.LOGO)) {
                companyDto.setLogo(file.getLink());
            }
        }

        return companyDto;
    }

    @Override
    public CompanyDto getCompanyById(long companyId) {
        Company company = returnCompanyOrThrowError(companyId);

        return getCompanyData(company);
    }

    @Override
    public List<CompanyDto> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();

        return companies.stream().map(this::getCompanyData).collect(Collectors.toList());
    }

    @Override
    public DetailedCompanyDto getCompanyDetailedData(long companyId) {
        Company company = returnCompanyOrThrowError(companyId);

        DetailedCompanyDto dto = new DetailedCompanyDto();
        dto.setId(companyId);
        dto.setName(company.getName());
        dto.setAbout(company.getAbout());
        dto.setWebsite(company.getWebsite());
        dto.setHeadLine(company.getHeadLine());
        dto.setNumEmployees(company.getNumEmployees());

        User user = company.getCreatedBy();
        dto.setCreatedBy(user.getId());

        setCompanyFiles(dto, company);
        setCompanyLocations(dto, company);

        return dto;
    }

    private String getFileId(String fileUrl) {
        String[] urlSplit = fileUrl.split("/");

        String url = null;

        if (urlSplit.length > 1){
            String[] urlSplit2 = urlSplit[urlSplit.length - 1].split("\\.");

            if(urlSplit2.length == 0) url = urlSplit[urlSplit.length - 1];
            else url = urlSplit2[0];
        }

        return url;
    }

    @Override
    public void deleteCompanyById(long companyId, long userId, boolean isAdmin) {
        Company company = returnCompanyOrThrowError(companyId);

        User user = company.getCreatedBy();
        if (!user.getId().equals(userId) && !isAdmin) throw new ApiException(HttpStatus.BAD_REQUEST, "You cannot delete someone else company");

        Set<CompanyFiles> companyFilesSet = company.getFiles();

        boolean toDeleteFile = false;
        String[] fileIds = null;

        if (companyFilesSet != null && !companyFilesSet.isEmpty()) {
            fileIds = new String[companyFilesSet.size()];

            int i = 0;
            for(CompanyFiles file: companyFilesSet) {
                String url = getFileId(file.getLink());

                if(url != null) {
                    fileIds[i] = url;
                    toDeleteFile = true;
                    i++;
                }
            }
        }

        if (toDeleteFile) {
            BatchFileDeleteDto fileDeleteDto = new BatchFileDeleteDto();
            fileDeleteDto.setFileIds(fileIds);

            try {
                this.fileService.batchDeleteFiles(fileDeleteDto);
            }
            catch (Exception e){
                e.printStackTrace();
                System.out.println("Error while deleting batch files");
            }
        }

        companyRepository.delete(company);
    }

    @Override
    public void deleteCompanyLocation(long companyLocationId) {
        CompanyLocations location = companyLocationsRepository.findById(companyLocationId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "company location with id " + companyLocationId + " was not found"));

        companyLocationsRepository.delete(location);
    }

    @Override
    public String uploadCompanyFile(MultipartFile file, long companyId, String fileType) {
        Company company = returnCompanyOrThrowError(companyId);

        CompanyFileType companyFileType;

        try {
            companyFileType = CompanyFileType.valueOf(fileType.toUpperCase());
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid File Type. Only " + CompanyFileType.LOGO + ", " + CompanyFileType.BANNER + " are supported");
        }

        String url;

        try {
            url = this.fileService.uploadFile(file);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while uploading file");
        }

        Set<CompanyFiles> companyFilesSet = company.getFiles();

        if (companyFilesSet != null && !companyFilesSet.isEmpty()) {
            for(CompanyFiles companyFile: companyFilesSet) {
                if (companyFile.getType().equals(companyFileType)) {
                    companyFile.setLink(url);
                    companyFilesRepository.save(companyFile);
                    return url;
                }
            }
        }

        CompanyFiles companyFile = new CompanyFiles();
        companyFile.setCompany(company);
        companyFile.setType(companyFileType);
        companyFile.setLink(url);

        companyFilesRepository.save(companyFile);

        return url;
    }

    @Override
    public void updateCompany(UpdateCompanyDto updateCompanyDto, long companyId) {
        Company company = returnCompanyOrThrowError(companyId);

        if(updateCompanyDto.getName() != null) company.setName(updateCompanyDto.getName());
        if(updateCompanyDto.getWebsite() != null) company.setWebsite(updateCompanyDto.getWebsite());
        if(updateCompanyDto.getHeadLine() != null) company.setHeadLine(updateCompanyDto.getHeadLine());
        if(updateCompanyDto.getNumEmployees() != null) company.setNumEmployees(updateCompanyDto.getNumEmployees());
        if(updateCompanyDto.getAbout() != null) company.setAbout(updateCompanyDto.getAbout());

        companyRepository.save(company);
    }

    @Override
    public void updateCompanyLocation(UpdateCompanyLocationDto updateCompanyLocationDto, long companyLocationId) {
        Optional<CompanyLocations> optionalLocation = companyLocationsRepository.findById(companyLocationId);

        if(optionalLocation.isEmpty()) throw new ApiException(HttpStatus.BAD_REQUEST, "company location with id " + companyLocationId + " was not found");

        CompanyLocations location = optionalLocation.get();

        if(updateCompanyLocationDto.getAddress() != null) location.setAddress(updateCompanyLocationDto.getAddress());
        if(updateCompanyLocationDto.getCity() != null) location.setCity(updateCompanyLocationDto.getCity());
        if(updateCompanyLocationDto.getZipCode() != null) location.setZipCode(updateCompanyLocationDto.getZipCode());
        if(updateCompanyLocationDto.getCountry() != null) location.setCountry(updateCompanyLocationDto.getCountry());

        companyLocationsRepository.save(location);
    }

    private void setCompanyLocations(DetailedCompanyDto dto, Company company) {
        Set<CompanyLocations> companyLocationsSet = company.getLocations();

        if (companyLocationsSet != null && !companyLocationsSet.isEmpty()) {
            List<CompanyLocations> companyLocations = companyLocationsSet.stream().toList();

            CompanyLocationDto[] companyLocationDtos = new CompanyLocationDto[companyLocations.size()];

            int i = 0;
            for(CompanyLocations location: companyLocations) {
                CompanyLocationDto companyLocationDto = new CompanyLocationDto();
                companyLocationDto.setId(location.getId());
                companyLocationDto.setCity(location.getCity());
                companyLocationDto.setAddress(location.getAddress());
                companyLocationDto.setCountry(location.getCountry());
                companyLocationDto.setZipCode(location.getZipCode());

                companyLocationDtos[i] = companyLocationDto;
                i++;
            }

            dto.setLocations(companyLocationDtos);
        }
    }

    private void setCompanyFiles(DetailedCompanyDto dto, Company company) {
        Set<CompanyFiles> companyFilesSet = company.getFiles();

        if (companyFilesSet != null && !companyFilesSet.isEmpty()) {
            List<CompanyFiles> companyFiles = companyFilesSet.stream().toList();

            for(CompanyFiles file: companyFiles) {
                if(file.getType().equals(CompanyFileType.LOGO)) {
                    dto.setLogo(file.getLink());
                }
                else {
                    dto.setBanner(file.getLink());
                }
            }
        }
    }

    private Company returnCompanyOrThrowError(long companyId) {
        Optional<Company> optionalCompany = companyRepository.findById(companyId);

        if (optionalCompany.isEmpty()) throw new ApiException(HttpStatus.BAD_REQUEST, "Company with id " + companyId + " does not exist");

        return optionalCompany.get();
    }
}
