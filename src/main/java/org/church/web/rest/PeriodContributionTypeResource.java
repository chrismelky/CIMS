package org.church.web.rest;

import org.church.domain.PeriodContributionType;
import org.church.service.PeriodContributionTypeService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.PeriodContributionTypeCriteria;
import org.church.service.PeriodContributionTypeQueryService;

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
 * REST controller for managing {@link org.church.domain.PeriodContributionType}.
 */
@RestController
@RequestMapping("/api")
public class PeriodContributionTypeResource {

    private final Logger log = LoggerFactory.getLogger(PeriodContributionTypeResource.class);

    private static final String ENTITY_NAME = "periodContributionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeriodContributionTypeService periodContributionTypeService;

    private final PeriodContributionTypeQueryService periodContributionTypeQueryService;

    public PeriodContributionTypeResource(PeriodContributionTypeService periodContributionTypeService, PeriodContributionTypeQueryService periodContributionTypeQueryService) {
        this.periodContributionTypeService = periodContributionTypeService;
        this.periodContributionTypeQueryService = periodContributionTypeQueryService;
    }

    /**
     * {@code POST  /period-contribution-types} : Create a new periodContributionType.
     *
     * @param periodContributionType the periodContributionType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new periodContributionType, or with status {@code 400 (Bad Request)} if the periodContributionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/period-contribution-types")
    public ResponseEntity<PeriodContributionType> createPeriodContributionType(@Valid @RequestBody PeriodContributionType periodContributionType) throws URISyntaxException {
        log.debug("REST request to save PeriodContributionType : {}", periodContributionType);
        if (periodContributionType.getId() != null) {
            throw new BadRequestAlertException("A new periodContributionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PeriodContributionType result = periodContributionTypeService.save(periodContributionType);
        return ResponseEntity.created(new URI("/api/period-contribution-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /period-contribution-types} : Updates an existing periodContributionType.
     *
     * @param periodContributionType the periodContributionType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periodContributionType,
     * or with status {@code 400 (Bad Request)} if the periodContributionType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the periodContributionType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/period-contribution-types")
    public ResponseEntity<PeriodContributionType> updatePeriodContributionType(@Valid @RequestBody PeriodContributionType periodContributionType) throws URISyntaxException {
        log.debug("REST request to update PeriodContributionType : {}", periodContributionType);
        if (periodContributionType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PeriodContributionType result = periodContributionTypeService.save(periodContributionType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, periodContributionType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /period-contribution-types} : get all the periodContributionTypes.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of periodContributionTypes in body.
     */
    @GetMapping("/period-contribution-types")
    public ResponseEntity<List<PeriodContributionType>> getAllPeriodContributionTypes(PeriodContributionTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PeriodContributionTypes by criteria: {}", criteria);
        Page<PeriodContributionType> page = periodContributionTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /period-contribution-types/count} : count all the periodContributionTypes.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/period-contribution-types/count")
    public ResponseEntity<Long> countPeriodContributionTypes(PeriodContributionTypeCriteria criteria) {
        log.debug("REST request to count PeriodContributionTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(periodContributionTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /period-contribution-types/:id} : get the "id" periodContributionType.
     *
     * @param id the id of the periodContributionType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the periodContributionType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/period-contribution-types/{id}")
    public ResponseEntity<PeriodContributionType> getPeriodContributionType(@PathVariable Long id) {
        log.debug("REST request to get PeriodContributionType : {}", id);
        Optional<PeriodContributionType> periodContributionType = periodContributionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(periodContributionType);
    }

    /**
     * {@code DELETE  /period-contribution-types/:id} : delete the "id" periodContributionType.
     *
     * @param id the id of the periodContributionType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/period-contribution-types/{id}")
    public ResponseEntity<Void> deletePeriodContributionType(@PathVariable Long id) {
        log.debug("REST request to delete PeriodContributionType : {}", id);
        periodContributionTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
