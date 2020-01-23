package org.church.web.rest;

import org.church.domain.PeriodType;
import org.church.service.PeriodTypeService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.PeriodTypeCriteria;
import org.church.service.PeriodTypeQueryService;

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
 * REST controller for managing {@link org.church.domain.PeriodType}.
 */
@RestController
@RequestMapping("/api")
public class PeriodTypeResource {

    private final Logger log = LoggerFactory.getLogger(PeriodTypeResource.class);

    private static final String ENTITY_NAME = "periodType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeriodTypeService periodTypeService;

    private final PeriodTypeQueryService periodTypeQueryService;

    public PeriodTypeResource(PeriodTypeService periodTypeService, PeriodTypeQueryService periodTypeQueryService) {
        this.periodTypeService = periodTypeService;
        this.periodTypeQueryService = periodTypeQueryService;
    }

    /**
     * {@code POST  /period-types} : Create a new periodType.
     *
     * @param periodType the periodType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new periodType, or with status {@code 400 (Bad Request)} if the periodType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/period-types")
    public ResponseEntity<PeriodType> createPeriodType(@Valid @RequestBody PeriodType periodType) throws URISyntaxException {
        log.debug("REST request to save PeriodType : {}", periodType);
        if (periodType.getId() != null) {
            throw new BadRequestAlertException("A new periodType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PeriodType result = periodTypeService.save(periodType);
        return ResponseEntity.created(new URI("/api/period-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /period-types} : Updates an existing periodType.
     *
     * @param periodType the periodType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periodType,
     * or with status {@code 400 (Bad Request)} if the periodType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the periodType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/period-types")
    public ResponseEntity<PeriodType> updatePeriodType(@Valid @RequestBody PeriodType periodType) throws URISyntaxException {
        log.debug("REST request to update PeriodType : {}", periodType);
        if (periodType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PeriodType result = periodTypeService.save(periodType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, periodType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /period-types} : get all the periodTypes.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of periodTypes in body.
     */
    @GetMapping("/period-types")
    public ResponseEntity<List<PeriodType>> getAllPeriodTypes(PeriodTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PeriodTypes by criteria: {}", criteria);
        Page<PeriodType> page = periodTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /period-types/count} : count all the periodTypes.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/period-types/count")
    public ResponseEntity<Long> countPeriodTypes(PeriodTypeCriteria criteria) {
        log.debug("REST request to count PeriodTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(periodTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /period-types/:id} : get the "id" periodType.
     *
     * @param id the id of the periodType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the periodType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/period-types/{id}")
    public ResponseEntity<PeriodType> getPeriodType(@PathVariable Long id) {
        log.debug("REST request to get PeriodType : {}", id);
        Optional<PeriodType> periodType = periodTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(periodType);
    }

    /**
     * {@code DELETE  /period-types/:id} : delete the "id" periodType.
     *
     * @param id the id of the periodType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/period-types/{id}")
    public ResponseEntity<Void> deletePeriodType(@PathVariable Long id) {
        log.debug("REST request to delete PeriodType : {}", id);
        periodTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
