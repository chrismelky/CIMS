package org.church.web.rest;

import org.church.domain.PeriodContributionItem;
import org.church.service.PeriodContributionItemService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.PeriodContributionItemCriteria;
import org.church.service.PeriodContributionItemQueryService;

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
 * REST controller for managing {@link org.church.domain.PeriodContributionItem}.
 */
@RestController
@RequestMapping("/api")
public class PeriodContributionItemResource {

    private final Logger log = LoggerFactory.getLogger(PeriodContributionItemResource.class);

    private static final String ENTITY_NAME = "periodContributionItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeriodContributionItemService periodContributionItemService;

    private final PeriodContributionItemQueryService periodContributionItemQueryService;

    public PeriodContributionItemResource(PeriodContributionItemService periodContributionItemService, PeriodContributionItemQueryService periodContributionItemQueryService) {
        this.periodContributionItemService = periodContributionItemService;
        this.periodContributionItemQueryService = periodContributionItemQueryService;
    }

    /**
     * {@code POST  /period-contribution-items} : Create a new periodContributionItem.
     *
     * @param periodContributionItem the periodContributionItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new periodContributionItem, or with status {@code 400 (Bad Request)} if the periodContributionItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/period-contribution-items")
    public ResponseEntity<PeriodContributionItem> createPeriodContributionItem(@Valid @RequestBody PeriodContributionItem periodContributionItem) throws URISyntaxException {
        log.debug("REST request to save PeriodContributionItem : {}", periodContributionItem);
        if (periodContributionItem.getId() != null) {
            throw new BadRequestAlertException("A new periodContributionItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PeriodContributionItem result = periodContributionItemService.save(periodContributionItem);
        return ResponseEntity.created(new URI("/api/period-contribution-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /period-contribution-items} : Updates an existing periodContributionItem.
     *
     * @param periodContributionItem the periodContributionItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated periodContributionItem,
     * or with status {@code 400 (Bad Request)} if the periodContributionItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the periodContributionItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/period-contribution-items")
    public ResponseEntity<PeriodContributionItem> updatePeriodContributionItem(@Valid @RequestBody PeriodContributionItem periodContributionItem) throws URISyntaxException {
        log.debug("REST request to update PeriodContributionItem : {}", periodContributionItem);
        if (periodContributionItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PeriodContributionItem result = periodContributionItemService.save(periodContributionItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, periodContributionItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /period-contribution-items} : get all the periodContributionItems.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of periodContributionItems in body.
     */
    @GetMapping("/period-contribution-items")
    public ResponseEntity<List<PeriodContributionItem>> getAllPeriodContributionItems(PeriodContributionItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PeriodContributionItems by criteria: {}", criteria);
        Page<PeriodContributionItem> page = periodContributionItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /period-contribution-items/count} : count all the periodContributionItems.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/period-contribution-items/count")
    public ResponseEntity<Long> countPeriodContributionItems(PeriodContributionItemCriteria criteria) {
        log.debug("REST request to count PeriodContributionItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(periodContributionItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /period-contribution-items/:id} : get the "id" periodContributionItem.
     *
     * @param id the id of the periodContributionItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the periodContributionItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/period-contribution-items/{id}")
    public ResponseEntity<PeriodContributionItem> getPeriodContributionItem(@PathVariable Long id) {
        log.debug("REST request to get PeriodContributionItem : {}", id);
        Optional<PeriodContributionItem> periodContributionItem = periodContributionItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(periodContributionItem);
    }

    /**
     * {@code DELETE  /period-contribution-items/:id} : delete the "id" periodContributionItem.
     *
     * @param id the id of the periodContributionItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/period-contribution-items/{id}")
    public ResponseEntity<Void> deletePeriodContributionItem(@PathVariable Long id) {
        log.debug("REST request to delete PeriodContributionItem : {}", id);
        periodContributionItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
