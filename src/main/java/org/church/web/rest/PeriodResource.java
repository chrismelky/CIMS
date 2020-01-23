package org.church.web.rest;

import org.church.domain.Period;
import org.church.service.PeriodService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.PeriodCriteria;
import org.church.service.PeriodQueryService;

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
 * REST controller for managing {@link org.church.domain.Period}.
 */
@RestController
@RequestMapping("/api")
public class PeriodResource {

    private final Logger log = LoggerFactory.getLogger(PeriodResource.class);

    private static final String ENTITY_NAME = "period";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeriodService periodService;

    private final PeriodQueryService periodQueryService;

    public PeriodResource(PeriodService periodService, PeriodQueryService periodQueryService) {
        this.periodService = periodService;
        this.periodQueryService = periodQueryService;
    }

    /**
     * {@code POST  /periods} : Create a new period.
     *
     * @param period the period to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new period, or with status {@code 400 (Bad Request)} if the period has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/periods")
    public ResponseEntity<Period> createPeriod(@Valid @RequestBody Period period) throws URISyntaxException {
        log.debug("REST request to save Period : {}", period);
        if (period.getId() != null) {
            throw new BadRequestAlertException("A new period cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Period result = periodService.save(period);
        return ResponseEntity.created(new URI("/api/periods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /periods} : Updates an existing period.
     *
     * @param period the period to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated period,
     * or with status {@code 400 (Bad Request)} if the period is not valid,
     * or with status {@code 500 (Internal Server Error)} if the period couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/periods")
    public ResponseEntity<Period> updatePeriod(@Valid @RequestBody Period period) throws URISyntaxException {
        log.debug("REST request to update Period : {}", period);
        if (period.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Period result = periodService.save(period);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, period.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /periods} : get all the periods.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of periods in body.
     */
    @GetMapping("/periods")
    public ResponseEntity<List<Period>> getAllPeriods(PeriodCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Periods by criteria: {}", criteria);
        Page<Period> page = periodQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /periods/count} : count all the periods.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/periods/count")
    public ResponseEntity<Long> countPeriods(PeriodCriteria criteria) {
        log.debug("REST request to count Periods by criteria: {}", criteria);
        return ResponseEntity.ok().body(periodQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /periods/:id} : get the "id" period.
     *
     * @param id the id of the period to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the period, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/periods/{id}")
    public ResponseEntity<Period> getPeriod(@PathVariable Long id) {
        log.debug("REST request to get Period : {}", id);
        Optional<Period> period = periodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(period);
    }

    /**
     * {@code DELETE  /periods/:id} : delete the "id" period.
     *
     * @param id the id of the period to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/periods/{id}")
    public ResponseEntity<Void> deletePeriod(@PathVariable Long id) {
        log.debug("REST request to delete Period : {}", id);
        periodService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
