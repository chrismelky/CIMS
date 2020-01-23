package org.church.web.rest;

import org.church.domain.PeriodContribution;
import org.church.service.PeriodContributionService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.PeriodContributionCriteria;
import org.church.service.PeriodContributionQueryService;

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
 * REST controller for managing {@link org.church.domain.PeriodContribution}.
 */
@RestController
@RequestMapping("/api")
public class PeriodContributionResource {

    private final Logger log = LoggerFactory.getLogger(PeriodContributionResource.class);

    private static final String ENTITY_NAME = "periodContribution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeriodContributionService periodContributionService;

    private final PeriodContributionQueryService periodContributionQueryService;

    public PeriodContributionResource(PeriodContributionService periodContributionService, PeriodContributionQueryService periodContributionQueryService) {
        this.periodContributionService = periodContributionService;
        this.periodContributionQueryService = periodContributionQueryService;
    }

    /**
     * {@code POST  /period-contributions} : Create a new periodContribution.
     *
     * @param periodContribution the periodContribution to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new periodContribution, or with status {@code 400 (Bad Request)} if the periodContribution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/period-contributions")
    public ResponseEntity<PeriodContribution> createPeriodContribution(@Valid @RequestBody PeriodContribution periodContribution) throws URISyntaxException {
        log.debug("REST request to save PeriodContribution : {}", periodContribution);
        if (periodContribution.getId() != null) {
            throw new BadRequestAlertException("A new periodContribution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PeriodContribution result = periodContributionService.save(periodContribution);
        return ResponseEntity.created(new URI("/api/period-contributions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /period-contributions} : Updates an existing periodContribution.
     *
     * @param periodContribution the periodContribution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periodContribution,
     * or with status {@code 400 (Bad Request)} if the periodContribution is not valid,
     * or with status {@code 500 (Internal Server Error)} if the periodContribution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/period-contributions")
    public ResponseEntity<PeriodContribution> updatePeriodContribution(@Valid @RequestBody PeriodContribution periodContribution) throws URISyntaxException {
        log.debug("REST request to update PeriodContribution : {}", periodContribution);
        if (periodContribution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PeriodContribution result = periodContributionService.save(periodContribution);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, periodContribution.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /period-contributions} : get all the periodContributions.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of periodContributions in body.
     */
    @GetMapping("/period-contributions")
    public ResponseEntity<List<PeriodContribution>> getAllPeriodContributions(PeriodContributionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PeriodContributions by criteria: {}", criteria);
        Page<PeriodContribution> page = periodContributionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /period-contributions/count} : count all the periodContributions.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/period-contributions/count")
    public ResponseEntity<Long> countPeriodContributions(PeriodContributionCriteria criteria) {
        log.debug("REST request to count PeriodContributions by criteria: {}", criteria);
        return ResponseEntity.ok().body(periodContributionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /period-contributions/:id} : get the "id" periodContribution.
     *
     * @param id the id of the periodContribution to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the periodContribution, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/period-contributions/{id}")
    public ResponseEntity<PeriodContribution> getPeriodContribution(@PathVariable Long id) {
        log.debug("REST request to get PeriodContribution : {}", id);
        Optional<PeriodContribution> periodContribution = periodContributionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(periodContribution);
    }

    /**
     * {@code DELETE  /period-contributions/:id} : delete the "id" periodContribution.
     *
     * @param id the id of the periodContribution to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/period-contributions/{id}")
    public ResponseEntity<Void> deletePeriodContribution(@PathVariable Long id) {
        log.debug("REST request to delete PeriodContribution : {}", id);
        periodContributionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
