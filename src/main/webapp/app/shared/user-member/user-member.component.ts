import { Component, Input, OnInit, Output } from '@angular/core';
import { MemberService } from 'app/entities/member/member.service';
import { IMember } from 'app/shared/model/member.model';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'church-user-member',
  templateUrl: './user-member.component.html',
  styleUrls: ['./user-member.component.scss']
})
export class UserMemberComponent implements OnInit {
  member: IMember;

  @Input() existingMember: IMember;
  @Output() onMemberChange = new BehaviorSubject<IMember>(null);

  constructor(private memberService: MemberService) {}

  ngOnInit() {}

  getMemberByRn(rn: string) {
    this.memberService.findByRn(rn).subscribe(resp => {
      this.member = resp.body;
    });
  }

  setSelected() {
    this.onMemberChange.next(this.member);
  }

  resetSelected() {
    this.onMemberChange.next(null);
  }
}
