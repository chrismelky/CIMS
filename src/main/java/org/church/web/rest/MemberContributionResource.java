package org.church.web.rest;

import org.church.domain.MemberContribution;
import org.church.service.MemberContributionService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.MemberContributionCriteria;
import org.church.service.MemberContributionQueryService;

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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.church.domain.MemberContribution}.
 */
@RestController
@RequestMapping("/api")
public class MemberContributionResource {

    private final Logger log = LoggerFactory.getLogger(MemberContributionResource.class);

    private static final String ENTITY_NAME = "memberContribution";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemberContributionService memberContributionService;

    private final MemberContributionQueryService memberContributionQueryService;

    public MemberContributionResource(MemberContributionService memberContributionService, MemberContributionQueryService memberContributionQueryService) {
        this.memberContributionService = memberContributionService;
        this.memberContributionQueryService = memberContributionQueryService;
    }

    /**
     * {@code POST  /member-contributions} : Create a new memberContribution.
     *
     * @param memberContribution the memberContribution to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memberContribution, or with status {@code 400 (Bad Request)} if the memberContribution has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/member-contributions")
    public ResponseEntity<MemberContribution> createMemberContribution(@Valid @RequestBody MemberContribution memberContribution) throws URISyntaxException {
        log.debug("REST request to save MemberContribution : {}", memberContribution);
        if (memberContribution.getId() != null) {
            throw new BadRequestAlertException("A new memberContribution cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MemberContribution result = memberContributionService.save(memberContribution);
        return ResponseEntity.created(new URI("/api/member-contributions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /member-contributions} : Updates an existing memberContribution.
     *
     * @param memberContribution the memberContribution to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberContribution,
     * or with status {@code 400 (Bad Request)} if the memberContribution is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memberContribution couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/member-contributions")
    public ResponseEntity<MemberContribution> updateMemberContribution(@Valid @RequestBody MemberContribution memberContribution) throws URISyntaxException {
        log.debug("REST request to update MemberContribution : {}", memberContribution);
        if (memberContribution.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MemberContribution result = memberContributionService.save(memberContribution);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memberContribution.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /member-contributions} : get all the memberContributions.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memberContributions in body.
     */
    @GetMapping("/member-contributions")
    public ResponseEntity<List<MemberContribution>> getAllMemberContributions(MemberContributionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MemberContributions by criteria: {}", criteria);
        Page<MemberContribution> page = memberContributionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/member-contributions/by-promise/{pId}")
        public ResponseEntity<List<MemberContribution>> getByPromise(@PathVariable("pId") Long pId, @RequestParam(name = "paymentDate", required = false) LocalDate date, Pageable pageable) {
            log.debug("REST request to get MemberContributions by promiseId: {}", pId);
            Page<MemberContribution> page = memberContributionQueryService.findByPromise(pId, date, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        }

    /**
    * {@code GET  /member-contributions/count} : count all the memberContributions.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/member-contributions/count")
    public ResponseEntity<Long> countMemberContributions(MemberContributionCriteria criteria) {
        log.debug("REST request to count MemberContributions by criteria: {}", criteria);
        return ResponseEntity.ok().body(memberContributionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /member-contributions/:id} : get the "id" memberContribution.
     *
     * @param id the id of the memberContribution to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memberContribution, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/member-contributions/{id}")
    public ResponseEntity<MemberContribution> getMemberContribution(@PathVariable Long id) {
        log.debug("REST request to get MemberContribution : {}", id);
        Optional<MemberContribution> memberContribution = memberContributionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(memberContribution);
    }

    /**
     * {@code DELETE  /member-contributions/:id} : delete the "id" memberContribution.
     *
     * @param id the id of the memberContribution to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/member-contributions/{id}")
    public ResponseEntity<Void> deleteMemberContribution(@PathVariable Long id) {
        log.debug("REST request to delete MemberContribution : {}", id);
        memberContributionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
