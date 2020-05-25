package org.church.config;

import org.church.domain.Member;
import org.church.repository.MemberRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberUploadWriter implements ItemWriter<Member> {

    @Autowired
    MemberRepository memberRepository;

    @Override
    public void write(List<? extends Member> members) throws Exception {
        members.forEach(m -> {
            Integer exist = 0;
            if (m.getMiddleName() != null) {
                exist = memberRepository.countByChurch_IdAndFirstNameAndMiddleNameAndLastName(m.getChurch().getId(), m.getFirstName(), m.getMiddleName(), m.getLastName());
            } else  {
                exist = memberRepository.countByChurch_IdAndFirstNameAndLastNameAndMiddleNameIsNull(m.getChurch().getId(),m.getFirstName(),m.getLastName());
            }
            if (exist == 0) {
                memberRepository.save(m);
            }
        });
    }
}

