package org.church.web.rest;

import org.church.domain.Member;
import org.church.service.MemberService;
import org.church.web.rest.errors.BadRequestAlertException;
import org.church.service.dto.MemberCriteria;
import org.church.service.MemberQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.church.domain.Member}.
 */
@RestController
@RequestMapping("/api")
public class MemberResource {

    private final Logger log = LoggerFactory.getLogger(MemberResource.class);

    private static final String ENTITY_NAME = "member";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemberService memberService;

    private final MemberQueryService memberQueryService;

    private final JobLauncher jobLauncher;

    private final Job memberUploadJob;

    public MemberResource(MemberService memberService, MemberQueryService memberQueryService, JobLauncher jobLauncher, Job memberUploadJob) {
        this.memberService = memberService;
        this.memberQueryService = memberQueryService;
        this.jobLauncher = jobLauncher;
        this.memberUploadJob = memberUploadJob;
    }

    /**
     * {@code POST  /members} : Create a new member.
     *
     * @param member the member to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new member, or with status {@code 400 (Bad Request)} if the member has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/members")
    public ResponseEntity<Member> createMember(@Valid @RequestBody Member member) throws URISyntaxException {
        log.debug("REST request to save Member : {}", member);
        if (member.getId() != null) {
            throw new BadRequestAlertException("A new member cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (member.getMiddleName() == null || member.getMiddleName().isEmpty()) {
            if (memberService.countByChurchFirstAndLastNames(
                member.getChurch().getId(),
                member.getFirstName(),
                member.getLastName()) > 0) {
                throw new BadRequestAlertException("Member already exist", ENTITY_NAME, "firstlastexists");
            }
        } else {
            if (memberService.countByChurchFirstNameMiddleNameLastName(
                member.getChurch().getId(),
                member.getFirstName(),
                member.getMiddleName(),
                member.getLastName()
            ) > 0) {
                throw new BadRequestAlertException("Member already exist", ENTITY_NAME, "firstmiddlelastexists");
            }
        }
        if (member.getChurchRn() != null) {
            Member byNumber = memberService.findByChurchRegistrationNumber(
                member.getChurch().getId(),
                member.getChurchRn(),
                null);
            if (byNumber != null) {
                throw new BadRequestAlertException("Member rn already exist",
                    byNumber.getFirstName()+' '+ byNumber.getLastName(),
                    "churchrnexist");
            }
        }
        member.setMemberRn(memberService.getMemberRn(member.getChurch()));
        Member result = memberService.save(member);
        return ResponseEntity.created(new URI("/api/members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /members} : Updates an existing member.
     *
     * @param member the member to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated member,
     * or with status {@code 400 (Bad Request)} if the member is not valid,
     * or with status {@code 500 (Internal Server Error)} if the member couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/members")
    public ResponseEntity<Member> updateMember(@Valid @RequestBody Member member) throws URISyntaxException {
        log.debug("REST request to update Member : {}", member);
        if (member.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (member.getMemberRn() == null) {
            member.setMemberRn(memberService.getMemberRn(member.getChurch()));
        }
        if (member.getMiddleName() == null || member.getMiddleName().isEmpty()) {
            if (memberService.countByChurchFirstAndLastNamesIdNot(
                member.getChurch().getId(),
                member.getFirstName(),
                member.getLastName(),
                member.getId()) > 0) {
                throw new BadRequestAlertException("Member already exist", ENTITY_NAME, "firstlastexists");
            }
        } else {
            if (memberService.countByChurchFirstNameMiddleNameLastNameIdNot(
                member.getChurch().getId(),
                member.getFirstName(),
                member.getMiddleName(),
                member.getLastName(),
                member.getId()
            ) > 0) {
                throw new BadRequestAlertException("Member already exist", ENTITY_NAME, "firstmiddlelastexists");
            }
        }
        if (member.getChurchRn() != null) {
            Member byNumber = memberService.findByChurchRegistrationNumber(
                member.getChurch().getId(),
                member.getChurchRn(),
                member.getId());
            if (byNumber != null) {
                throw new BadRequestAlertException("Member rn already exist",
                    byNumber.getFirstName()+' '+ byNumber.getLastName(),
                    "churchrnexist");
            }
        }
        Member result = memberService.save(member);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, member.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /members} : get all the members.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of members in body.
     */
    @GetMapping("/members")
    public ResponseEntity<List<Member>> getAllMembers(MemberCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Members by criteria: {}", criteria);
        Page<Member> page = memberQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /members/count} : count all the members.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/members/count")
    public ResponseEntity<Long> countMembers(MemberCriteria criteria) {
        log.debug("REST request to count Members by criteria: {}", criteria);
        return ResponseEntity.ok().body(memberQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /members/:id} : get the "id" member.
     *
     * @param id the id of the member to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the member, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/members/{id}")
    public ResponseEntity<Member> getMember(@PathVariable Long id) {
        log.debug("REST request to get Member : {}", id);
        Optional<Member> member = memberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(member);
    }


    @GetMapping("/members/byRn/{memberRn}")
    public ResponseEntity<Member> getMemberByRn(@PathVariable String memberRn) {
        log.debug("REST request to get Member : {}", memberRn);
        Optional<Member> member = memberService.findByMemberRn(memberRn);
        return ResponseUtil.wrapOrNotFound(member);
    }

    /**
     * {@code DELETE  /members/:id} : delete the "id" member.
     *
     * @param id the id of the member to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        log.debug("REST request to delete Member : {}", id);
        memberService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("members/{churchId}/upload")
    public String uploadData(@RequestParam("file") MultipartFile file, @PathVariable("churchId") Long churchId, @RequestParam("fyId") Long fyId) throws IOException,
        JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        File tmpFile = File.createTempFile("cim-member",".csv");
        Files.copy(file.getInputStream(), tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        JobExecution jobExecution = jobLauncher.run(memberUploadJob, new JobParametersBuilder()
            .addString("filePath", tmpFile.getAbsolutePath())
            .addLong("time", System.currentTimeMillis())
            .addLong("churchId", churchId)
            .addLong("fyId", fyId)
            .toJobParameters());

        BatchStatus status = jobExecution.getStatus();
        System.out.println(status);
        if (status == BatchStatus.FAILED) {
            return "Error at row " +String.valueOf(jobExecution.getExecutionContext().getInt("Row", 0))+"; column="+ jobExecution.getExecutionContext().getString("Column","");
        } else {
            return String.valueOf(BatchStatus.COMPLETED);
        }

    }
}
