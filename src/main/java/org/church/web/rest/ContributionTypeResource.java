package org.church.web.rest;

import org.church.domain.ContributionType;
import org.church.service.ContributionTypeService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.ContributionTypeCriteria;
import org.church.service.ContributionTypeQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.church.domain.ContributionType}.
 */
@RestController
@RequestMapping("/api")
public class ContributionTypeResource {

    private final Logger log = LoggerFactory.getLogger(ContributionTypeResource.class);

    private static final String ENTITY_NAME = "contributionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContributionTypeService contributionTypeService;

    private final ContributionTypeQueryService contributionTypeQueryService;

    public ContributionTypeResource(ContributionTypeService contributionTypeService, ContributionTypeQueryService contributionTypeQueryService) {
        this.contributionTypeService = contributionTypeService;
        this.contributionTypeQueryService = contributionTypeQueryService;
    }

    /**
     * {@code POST  /contribution-types} : Create a new contributionType.
     *
     * @param contributionType the contributionType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contributionType, or with status {@code 400 (Bad Request)} if the contributionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contribution-types")
    public ResponseEntity<ContributionType> createContributionType(@Valid @RequestBody ContributionType contributionType) throws URISyntaxException {
        log.debug("REST request to save ContributionType : {}", contributionType);
        if (contributionType.getId() != null) {
            throw new BadRequestAlertException("A new contributionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContributionType result = contributionTypeService.save(contributionType);
        return ResponseEntity.created(new URI("/api/contribution-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contribution-types} : Updates an existing contributionType.
     *
     * @param contributionType the contributionType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contributionType,
     * or with status {@code 400 (Bad Request)} if the contributionType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contributionType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contribution-types")
    public ResponseEntity<ContributionType> updateContributionType(@Valid @RequestBody ContributionType contributionType) throws URISyntaxException {
        log.debug("REST request to update ContributionType : {}", contributionType);
        if (contributionType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContributionType result = contributionTypeService.save(contributionType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contributionType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contribution-types} : get all the contributionTypes.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contributionTypes in body.
     */
    @GetMapping("/contribution-types")
    public ResponseEntity<List<ContributionType>> getAllContributionTypes(ContributionTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ContributionTypes by criteria: {}", criteria);
        Page<ContributionType> page = contributionTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /contribution-types/count} : count all the contributionTypes.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/contribution-types/count")
    public ResponseEntity<Long> countContributionTypes(ContributionTypeCriteria criteria) {
        log.debug("REST request to count ContributionTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(contributionTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contribution-types/:id} : get the "id" contributionType.
     *
     * @param id the id of the contributionType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contributionType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contribution-types/{id}")
    public ResponseEntity<ContributionType> getContributionType(@PathVariable Long id) {
        log.debug("REST request to get ContributionType : {}", id);
        Optional<ContributionType> contributionType = contributionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contributionType);
    }

    /**
     * {@code DELETE  /contribution-types/:id} : delete the "id" contributionType.
     *
     * @param id the id of the contributionType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contribution-types/{id}")
    public ResponseEntity<Void> deleteContributionType(@PathVariable Long id) {
        log.debug("REST request to delete ContributionType : {}", id);
        contributionTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
