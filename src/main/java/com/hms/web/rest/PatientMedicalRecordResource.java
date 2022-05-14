package com.hms.web.rest;

import com.hms.domain.PatientMedicalRecord;
import com.hms.domain.Wallet;
import com.hms.repository.PatientMedicalRecordRepository;
import com.hms.repository.WalletRepository;
import com.hms.service.BlockChainService;
import com.hms.service.dto.PatientMedicalRecordDTO;
import com.hms.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hms.domain.PatientMedicalRecord}.
 */
@RestController
@RequestMapping("/api")
public class PatientMedicalRecordResource {

    private final Logger log = LoggerFactory.getLogger(PatientMedicalRecordResource.class);

    private static final String ENTITY_NAME = "patientMedicalRecord";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatientMedicalRecordRepository patientMedicalRecordRepository;

    private final WalletRepository walletRepository;

    private final BlockChainService blockChainService;

    public PatientMedicalRecordResource(PatientMedicalRecordRepository patientMedicalRecordRepository,
                                        WalletRepository walletRepository, BlockChainService blockChainService) {
        this.patientMedicalRecordRepository = patientMedicalRecordRepository;
        this.walletRepository = walletRepository;
        this.blockChainService = blockChainService;
    }

    /**
     * {@code POST  /patient-medical-records} : Create a new patientMedicalRecord.
     *
     * @param patientMedicalRecord the patientMedicalRecord to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patientMedicalRecord, or with status {@code 400 (Bad Request)} if the patientMedicalRecord has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patient-medical-records/{id}")
    public ResponseEntity<PatientMedicalRecord> createPatientMedicalRecord(@RequestBody PatientMedicalRecord patientMedicalRecord, @PathVariable String id)
        throws URISyntaxException {
        log.debug("REST request to save PatientMedicalRecord : {}", patientMedicalRecord);
        if (patientMedicalRecord.getId() != null) {
            throw new BadRequestAlertException("A new patientMedicalRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<Wallet> optionalWallet = this.walletRepository.findById(id);
        if(optionalWallet.isPresent()) {
            blockChainService.mineNewBlock(patientMedicalRecord, optionalWallet.get());
        }
        return ResponseEntity
            .created(new URI("/api/patient-medical-records/"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, patientMedicalRecord.getPatientCondition()))
            .body(patientMedicalRecord);
    }

    /**
     * {@code PUT  /patient-medical-records/:id} : Updates an existing patientMedicalRecord.
     *
     * @param id the id of the patientMedicalRecord to save.
     * @param patientMedicalRecord the patientMedicalRecord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientMedicalRecord,
     * or with status {@code 400 (Bad Request)} if the patientMedicalRecord is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patientMedicalRecord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patient-medical-records/{id}")
    public ResponseEntity<PatientMedicalRecord> updatePatientMedicalRecord(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody PatientMedicalRecord patientMedicalRecord
    ) throws URISyntaxException {
        log.debug("REST request to update PatientMedicalRecord : {}, {}", id, patientMedicalRecord);
        if (patientMedicalRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientMedicalRecord.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientMedicalRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PatientMedicalRecord result = patientMedicalRecordRepository.save(patientMedicalRecord);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, patientMedicalRecord.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /patient-medical-records/:id} : Partial updates given fields of an existing patientMedicalRecord, field will ignore if it is null
     *
     * @param id the id of the patientMedicalRecord to save.
     * @param patientMedicalRecord the patientMedicalRecord to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientMedicalRecord,
     * or with status {@code 400 (Bad Request)} if the patientMedicalRecord is not valid,
     * or with status {@code 404 (Not Found)} if the patientMedicalRecord is not found,
     * or with status {@code 500 (Internal Server Error)} if the patientMedicalRecord couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/patient-medical-records/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PatientMedicalRecord> partialUpdatePatientMedicalRecord(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody PatientMedicalRecord patientMedicalRecord
    ) throws URISyntaxException {
        log.debug("REST request to partial update PatientMedicalRecord partially : {}, {}", id, patientMedicalRecord);
        if (patientMedicalRecord.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientMedicalRecord.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientMedicalRecordRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PatientMedicalRecord> result = patientMedicalRecordRepository
            .findById(patientMedicalRecord.getId())
            .map(existingPatientMedicalRecord -> {
                if (patientMedicalRecord.getPatientCondition() != null) {
                    existingPatientMedicalRecord.setPatientCondition(patientMedicalRecord.getPatientCondition());
                }
                if (patientMedicalRecord.getInsuranceClaimed() != null) {
                    existingPatientMedicalRecord.setInsuranceClaimed(patientMedicalRecord.getInsuranceClaimed());
                }

                return existingPatientMedicalRecord;
            })
            .map(patientMedicalRecordRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, patientMedicalRecord.getId())
        );
    }

    /**
     * {@code GET  /patient-medical-records} : get all the patientMedicalRecords.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patientMedicalRecords in body.
     */
    @GetMapping("/patient-medical-records/all/{id}")
    public List<PatientMedicalRecordDTO> getAllPatientMedicalRecords(@PathVariable String id) {
        log.debug("REST request to get all PatientMedicalRecords");
        Optional<Wallet> walletRepositoryById = walletRepository.findById(id);
        if(walletRepositoryById.isPresent()){
            return blockChainService.getMedicalHistory(walletRepositoryById.get());
        }
        return new ArrayList<>();
    }


    /**
     * {@code GET  /patient-medical-records/:id} : get the "id" patientMedicalRecord.
     *
     * @param id the id of the patientMedicalRecord to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patientMedicalRecord, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patient-medical-records/{id}")
    public ResponseEntity<PatientMedicalRecord> getPatientMedicalRecord(@PathVariable String id) {
        log.debug("REST request to get PatientMedicalRecord : {}", id);
        Optional<PatientMedicalRecord> patientMedicalRecord = patientMedicalRecordRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(patientMedicalRecord);
    }

    /**
     * {@code DELETE  /patient-medical-records/:id} : delete the "id" patientMedicalRecord.
     *
     * @param id the id of the patientMedicalRecord to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patient-medical-records/{id}")
    public ResponseEntity<Void> deletePatientMedicalRecord(@PathVariable String id) {
        log.debug("REST request to delete PatientMedicalRecord : {}", id);
        patientMedicalRecordRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
