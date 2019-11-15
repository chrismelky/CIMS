package org.church.service;

import org.church.domain.MemberPromise;
import org.church.repository.MemberPromiseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link MemberPromise}.
 */
@Service
@Transactional
public class MemberPromiseService {

    private final Logger log = LoggerFactory.getLogger(MemberPromiseService.class);

    private final MemberPromiseRepository memberPromiseRepository;

    public MemberPromiseService(MemberPromiseRepository memberPromiseRepository) {
        this.memberPromiseRepository = memberPromiseRepository;
    }

    /**
     * Save a memberPromise.
     *
     * @param memberPromise the entity to save.
     * @return the persisted entity.
     */
    public MemberPromise save(MemberPromise memberPromise) {
        log.debug("Request to save MemberPromise : {}", memberPromise);
        return memberPromiseRepository.save(memberPromise);
    }

    /**
     * Get all the memberPromises.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MemberPromise> findAll(Pageable pageable) {
        log.debug("Request to get all MemberPromises");
        return memberPromiseRepository.findAll(pageable);
    }


    /**
     * Get one memberPromise by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MemberPromise> findOne(Long id) {
        log.debug("Request to get MemberPromise : {}", id);
        return memberPromiseRepository.findById(id);
    }

    /**
     * Delete the memberPromise by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MemberPromise : {}", id);
        memberPromiseRepository.deleteById(id);
    }
}
