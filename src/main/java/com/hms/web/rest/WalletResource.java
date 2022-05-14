package com.hms.web.rest;

import com.hms.domain.Authority;
import com.hms.domain.User;
import com.hms.domain.Wallet;
import com.hms.repository.AuthorityRepository;
import com.hms.repository.UserRepository;
import com.hms.repository.WalletRepository;
import com.hms.security.AuthoritiesConstants;
import com.hms.security.SecurityUtils;
import com.hms.service.dto.PatientUserDTO;
import com.hms.service.dto.WalletDTO;
import com.hms.service.dto.WalletUserDTO;
import com.hms.service.mapper.WalletMapper;
import com.hms.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hms.domain.Wallet}.
 */
@RestController
@RequestMapping("/api")
public class WalletResource {

    private final Logger log = LoggerFactory.getLogger(WalletResource.class);

    private static final String ENTITY_NAME = "wallet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WalletRepository walletRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final UserRepository userRepository;

    private final WalletMapper walletMapper;

    public WalletResource(WalletRepository walletRepository, AuthorityRepository authorityRepository,
                          UserRepository userRepository, PasswordEncoder passwordEncoder, WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.walletMapper = walletMapper;
    }

    /**
     * {@code POST  /wallets} : Create a new wallet.
     *
     * @param wallet the wallet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wallet, or with status {@code 400 (Bad Request)} if the wallet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wallets")
    public ResponseEntity<Wallet> createWallet(@RequestBody Wallet wallet) throws URISyntaxException {
        log.debug("REST request to save Wallet : {}", wallet);
        if (wallet.getId() != null) {
            throw new BadRequestAlertException("A new wallet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Optional<Wallet> optionalWallet = walletRepository.findOneByUserId(wallet.getUserId());
        if(optionalWallet.isPresent()) {
            throw new BadRequestAlertException("User already linked to another wallet", ENTITY_NAME, "idexists");
        }
        wallet.generateKeys();
        Wallet result = walletRepository.save(wallet);
        Optional<User> optionalUser = userRepository.findById(wallet.getUserId());
        if(optionalUser.isPresent()) {
            optionalUser.get().setPassword(passwordEncoder.encode(wallet.getPrivateKey()));
            userRepository.save(optionalUser.get());
        }
        return ResponseEntity
            .created(new URI("/api/wallets/" + result.getId()))
            .headers(HeaderUtil.createAlert(applicationName, "A wallet is created with private key " + wallet.getPrivateKey(), wallet.getPrivateKey()))
            .body(result);
    }

    /**
     * {@code PUT  /wallets/:id} : Updates an existing wallet.
     *
     * @param id the id of the wallet to save.
     * @param wallet the wallet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wallet,
     * or with status {@code 400 (Bad Request)} if the wallet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wallet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wallets/{id}")
    public ResponseEntity<Wallet> updateWallet(@PathVariable(value = "id", required = false) final String id, @RequestBody Wallet wallet)
        throws URISyntaxException {
        log.debug("REST request to update Wallet : {}, {}", id, wallet);
        if (wallet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wallet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!walletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Wallet result = walletRepository.save(wallet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, wallet.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /wallets/:id} : Partial updates given fields of an existing wallet, field will ignore if it is null
     *
     * @param id the id of the wallet to save.
     * @param wallet the wallet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wallet,
     * or with status {@code 400 (Bad Request)} if the wallet is not valid,
     * or with status {@code 404 (Not Found)} if the wallet is not found,
     * or with status {@code 500 (Internal Server Error)} if the wallet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wallets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Wallet> partialUpdateWallet(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Wallet wallet
    ) throws URISyntaxException {
        log.debug("REST request to partial update Wallet partially : {}, {}", id, wallet);
        if (wallet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wallet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!walletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Wallet> result = walletRepository
            .findById(wallet.getId())
            .map(existingWallet -> {
                if (wallet.getStreet() != null) {
                    existingWallet.setStreet(wallet.getStreet());
                }
                if (wallet.getCity() != null) {
                    existingWallet.setCity(wallet.getCity());
                }
                if (wallet.getState() != null) {
                    existingWallet.setState(wallet.getState());
                }
                if (wallet.getCountry() != null) {
                    existingWallet.setCountry(wallet.getCountry());
                }
                if (wallet.getPostCode() != null) {
                    existingWallet.setPostCode(wallet.getPostCode());
                }
                if (wallet.getAge() != null) {
                    existingWallet.setAge(wallet.getAge());
                }
                if (wallet.getPhone() != null) {
                    existingWallet.setPhone(wallet.getPhone());
                }
                if (wallet.getPublicKey() != null) {
                    existingWallet.setPublicKey(wallet.getPublicKey());
                }
                if (wallet.getPrivateKey() != null) {
                    existingWallet.setPrivateKey(wallet.getPrivateKey());
                }
                if (wallet.getUserId() != null) {
                    existingWallet.setUserId(wallet.getUserId());
                }

                return existingWallet;
            })
            .map(walletRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, wallet.getId()));
    }

    /**
     * {@code GET  /wallets} : get all the wallets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wallets in body.
     */
    @GetMapping("/wallets/type/{type}")
    public List<WalletDTO> getAllWallets(@PathVariable String type) {
        log.debug("REST request to get all Wallets of type " + type);
        Optional<Authority> optionalAuthority = authorityRepository.findById(AuthoritiesConstants.getConstantByType(type));
        if(optionalAuthority.isPresent()){
            List<Wallet> walletList = walletRepository.findAll().stream().filter(wallet -> {
                Optional<User> optionalUser = userRepository.findById(wallet.getUserId());
                if (optionalUser.isPresent()) {
                    return optionalUser.get().getAuthorities().iterator().next().getName().equalsIgnoreCase(optionalAuthority.get().getName());
                }
                return false;
            }).collect(Collectors.toList());
            if(!walletList.isEmpty()){
                return walletMapper.toDto(walletList);
            }
            return new ArrayList<>();
        }
        return walletMapper.toDto(walletRepository.findAll());
    }

    /**
     * {@code GET  /wallets} : get all the wallets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wallets in body.
     */
    @GetMapping("/wallets/patient/{loginOrEmail}")
    public List<Wallet> getPatientWallets(@PathVariable String loginOrEmail) {
        log.debug("REST request to get all Wallets of type patient with login or email " + loginOrEmail);
        Optional<Authority> optionalAuthority = authorityRepository.findById(AuthoritiesConstants.getConstantByType("patient"));
        if(optionalAuthority.isPresent()){
            List<Wallet> walletList = walletRepository.findAll().stream().filter(wallet -> {
                Optional<User> optionalUser = userRepository.findById(wallet.getUserId());
                if (optionalUser.isPresent()) {
                    return optionalUser.get().getAuthorities().iterator().next().getName().equalsIgnoreCase(optionalAuthority.get().getName());
                }
                return false;
            }).collect(Collectors.toList());
            if(!walletList.isEmpty()){
                for(Wallet wallet: walletList) {
                    Optional<User> optionalUser = userRepository.findById(wallet.getUserId());
                    if(optionalUser.isPresent()) {
                        if(optionalUser.get().getEmail().equalsIgnoreCase(loginOrEmail) || optionalUser.get().getLogin().equalsIgnoreCase(loginOrEmail)){
                            walletList.clear();
                            walletList.add(wallet);
                            return walletList;
                        }
                    }
                }
                return new ArrayList<>();
            }
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    /**
     * {@code GET  /wallets} : get all the wallets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wallets in body.
     */
    @PostMapping("/wallets/patient/login")
    public ResponseEntity<WalletUserDTO> getPatientWallets(@RequestBody PatientUserDTO patientUserDTO) {
        log.debug("REST request to get Wallets of type patient with login " + patientUserDTO.getLogin());
        Optional<Authority> optionalAuthority = authorityRepository.findById(AuthoritiesConstants.getConstantByType("patient"));
        if(optionalAuthority.isPresent()){
            List<Wallet> walletList = walletRepository.findAll().stream().filter(wallet -> {
                Optional<User> optionalUser = userRepository.findById(wallet.getUserId());
                if (optionalUser.isPresent()) {
                    return optionalUser.get().getAuthorities().iterator().next().getName().equalsIgnoreCase(optionalAuthority.get().getName());
                }
                return false;
            }).collect(Collectors.toList());
            if(!walletList.isEmpty()){
                for(Wallet wallet: walletList) {
                    Optional<User> optionalUser = userRepository.findById(wallet.getUserId());
                    if(optionalUser.isPresent() && optionalUser.get().getLogin().equalsIgnoreCase(patientUserDTO.getLogin()) && wallet.getPrivateKey().equals(patientUserDTO.getPassword())) {
                        WalletUserDTO walletUserDTO = new WalletUserDTO();
                        walletUserDTO.setWallet(wallet);
                        walletUserDTO.setUser(optionalUser.get());
                        return ResponseUtil.wrapOrNotFound(Optional.of(walletUserDTO));
                    }
                }
                return ResponseUtil.wrapOrNotFound(Optional.empty());
            }
            return ResponseUtil.wrapOrNotFound(Optional.empty());
        }
        return ResponseUtil.wrapOrNotFound(Optional.empty());
    }

    /**
     * {@code GET  /wallets} : get all the wallets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wallets in body.
     */
    @GetMapping("/wallets")
    public List<Wallet> getAllWallets() {
        log.debug("REST request to get all Wallets");
        return walletRepository.findAll();
    }

    /**
     * {@code GET  /wallets/:id} : get the "id" wallet.
     *
     * @param id the id of the wallet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wallet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wallets/{id}")
    public ResponseEntity<Wallet> getWallet(@PathVariable String id) {
        log.debug("REST request to get Wallet : {}", id);
        Optional<Wallet> wallet = walletRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(wallet);
    }

    /**
     * {@code DELETE  /wallets/:id} : delete the "id" wallet.
     *
     * @param id the id of the wallet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wallets/{id}")
    public ResponseEntity<Void> deleteWallet(@PathVariable String id) {
        log.debug("REST request to delete Wallet : {}", id);
        walletRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
