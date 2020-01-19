import { Component, Input, OnInit, Output } from '@angular/core';
import { MemberService } from 'app/entities/member/member.service';
import { IMember, Member } from 'app/shared/model/member.model';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'church-user-member',
  templateUrl: './user-member.component.html',
  styleUrls: ['./user-member.component.scss']
})
export class UserMemberComponent implements OnInit {
  member: Member;

  @Input() existingMember: IMember;
  @Output() onMemberChange = new BehaviorSubject<Member>(null);

  constructor(private memberService: MemberService) {}

  ngOnInit() {}

  getMemberByRn(rn: string) {
    this.memberService.findByRn(rn).subscribe(resp => {
      this.member = resp.body;
    });
  }

  setMember() {
    this.onMemberChange.next(this.member);
  }

  resetMember() {
    this.onMemberChange.next(null);
  }
}
