package org.church.service;

import org.church.domain.MemberRelative;
import org.church.repository.MemberRelativeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link MemberRelative}.
 */
@Service
@Transactional
public class MemberRelativeService {

    private final Logger log = LoggerFactory.getLogger(MemberRelativeService.class);

    private final MemberRelativeRepository memberRelativeRepository;

    public MemberRelativeService(MemberRelativeRepository memberRelativeRepository) {
        this.memberRelativeRepository = memberRelativeRepository;
    }

    /**
     * Save a memberRelative.
     *
     * @param memberRelative the entity to save.
     * @return the persisted entity.
     */
    public MemberRelative save(MemberRelative memberRelative) {
        log.debug("Request to save MemberRelative : {}", memberRelative);
        return memberRelativeRepository.save(memberRelative);
    }

    /**
     * Get all the memberRelatives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MemberRelative> findAll(Pageable pageable) {
        log.debug("Request to get all MemberRelatives");
        return memberRelativeRepository.findAll(pageable);
    }


    /**
     * Get one memberRelative by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MemberRelative> findOne(Long id) {
        log.debug("Request to get MemberRelative : {}", id);
        return memberRelativeRepository.findById(id);
    }

    /**
     * Delete the memberRelative by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MemberRelative : {}", id);
        memberRelativeRepository.deleteById(id);
    }
}
