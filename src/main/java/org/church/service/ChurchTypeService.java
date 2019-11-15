package org.church.service;

import org.church.domain.ChurchType;
import org.church.repository.ChurchTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ChurchType}.
 */
@Service
@Transactional
public class ChurchTypeService {

    private final Logger log = LoggerFactory.getLogger(ChurchTypeService.class);

    private final ChurchTypeRepository churchTypeRepository;

    public ChurchTypeService(ChurchTypeRepository churchTypeRepository) {
        this.churchTypeRepository = churchTypeRepository;
    }

    /**
     * Save a churchType.
     *
     * @param churchType the entity to save.
     * @return the persisted entity.
     */
    public ChurchType save(ChurchType churchType) {
        log.debug("Request to save ChurchType : {}", churchType);
        return churchTypeRepository.save(churchType);
    }

    /**
     * Get all the churchTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChurchType> findAll(Pageable pageable) {
        log.debug("Request to get all ChurchTypes");
        return churchTypeRepository.findAll(pageable);
    }


    /**
     * Get one churchType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChurchType> findOne(Long id) {
        log.debug("Request to get ChurchType : {}", id);
        return churchTypeRepository.findById(id);
    }

    /**
     * Delete the churchType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ChurchType : {}", id);
        churchTypeRepository.deleteById(id);
    }
}
