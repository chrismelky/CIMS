import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'church-week-pick',
  templateUrl: './week-pick.component.html',
  styleUrls: ['./week-pick.component.scss']
})
export class WeekPickComponent implements OnInit {
  sunday = [];
  today;
  month;
  year;

  constructor() {}

  ngOnInit() {
    this.today = new Date();
    this.month = this.today.getMonth();
    this.year = this.today.getFullYear();
    this.getSunday(new Date(), 4);
  }
  getSunday(startDate, weekBack) {
    const d = new Date(startDate);
    d.setDate(d.getDate() - d.getDay());
    while (weekBack > 0) {
      d.setDate(d.getDate() - 7);
      this.sunday.push(d);
      --weekBack;
    }
  }
}
