package org.church.service;

import org.apache.commons.lang3.StringUtils;
import org.church.domain.Church;
import org.church.domain.Member;
import org.church.repository.ChurchRepository;
import org.church.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Member}.
 */
@Service
@Transactional
public class MemberService {

    private final Logger log = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;

    private final ChurchRepository churchRepository;

    public MemberService(MemberRepository memberRepository, ChurchRepository churchRepository) {
        this.memberRepository = memberRepository;
        this.churchRepository = churchRepository;
    }

    /**
     * Save a member.
     *
     * @param member the entity to save.
     * @return the persisted entity.
     */
    public Member save(Member member) {
        log.debug("Request to save Member : {}", member);
        return memberRepository.save(member);
    }

    /**
     * Get all the members.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Member> findAll(Pageable pageable) {
        log.debug("Request to get all Members");
        return memberRepository.findAll(pageable);
    }

    /**
     * Get all the members with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Member> findAllWithEagerRelationships(Pageable pageable) {
        return memberRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one member by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Member> findOne(Long id) {
        log.debug("Request to get Member : {}", id);
        return memberRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the member by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Member : {}", id);
        memberRepository.deleteById(id);
    }

    public Optional<Member> findByMemberRn(String memberRn) {
        return memberRepository.findByMemberRn(memberRn);
    }

    public String getMemberRn(Church church) {
        Long n = memberRepository.countAllByChurch(church);
        n =n +1;
        return  church.getId().toString().concat("-").concat(StringUtils.leftPad(n.toString(), 6, "0"));
    }
}
