package org.church.service;

import org.church.domain.MemberRite;
import org.church.repository.MemberRiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link MemberRite}.
 */
@Service
@Transactional
public class MemberRiteService {

    private final Logger log = LoggerFactory.getLogger(MemberRiteService.class);

    private final MemberRiteRepository memberRiteRepository;

    public MemberRiteService(MemberRiteRepository memberRiteRepository) {
        this.memberRiteRepository = memberRiteRepository;
    }

    /**
     * Save a memberRite.
     *
     * @param memberRite the entity to save.
     * @return the persisted entity.
     */
    public MemberRite save(MemberRite memberRite) {
        log.debug("Request to save MemberRite : {}", memberRite);
        return memberRiteRepository.save(memberRite);
    }

    /**
     * Get all the memberRites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MemberRite> findAll(Pageable pageable) {
        log.debug("Request to get all MemberRites");
        return memberRiteRepository.findAll(pageable);
    }


    /**
     * Get one memberRite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MemberRite> findOne(Long id) {
        log.debug("Request to get MemberRite : {}", id);
        return memberRiteRepository.findById(id);
    }

    /**
     * Delete the memberRite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MemberRite : {}", id);
        memberRiteRepository.deleteById(id);
    }
}
