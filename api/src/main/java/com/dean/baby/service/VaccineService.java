package com.dean.baby.common.service;

import com.dean.baby.common.entity.Vaccine;
import com.dean.baby.common.repository.VaccineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class VaccineService {

    private final VaccineRepository vaccineRepository;

    public VaccineService(VaccineRepository vaccineRepository) {
        this.vaccineRepository = vaccineRepository;
    }

    /**
     * 新增或更新疫苗資料
     */
    public Vaccine saveVaccine(Vaccine vaccine) {
        return vaccineRepository.save(vaccine);
    }

    /**
     * 依 ID 取得疫苗
     */
    @Transactional(readOnly = true)
    public Optional<Vaccine> findById(UUID id) {
        return vaccineRepository.findById(id);
    }

    /**
     * 取得所有疫苗
     */
    @Transactional(readOnly = true)
    public List<Vaccine> findAll() {
        return vaccineRepository.findAll();
    }

    /**
     * 依 ID 刪除疫苗
     */
    public void deleteById(UUID id) {
        vaccineRepository.deleteById(id);
    }
}
