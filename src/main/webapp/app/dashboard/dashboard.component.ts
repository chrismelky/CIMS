import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'church-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  dataSource: Object;
  chartConfig: Object;

  constructor() {
    this.chartConfig = {
      width: '800',
      height: '400',
      type: 'mscolumn2d',
      dataFormat: 'json'
    };

    this.dataSource = {
      chart: {
        caption: 'Ahadi za Michango na Makusanyo',
        subcaption: '2020',
        xaxisname: 'Aina ya Machango',
        yaxisname: 'Amount in Tshs',
        formatnumberscale: '1',
        plottooltext: '<b>$dataValue</b> apps were available on <b>$seriesName</b> in $label',
        theme: 'fusion',
        drawcrossline: '1'
      },
      categories: [
        {
          category: [
            {
              label: 'Ujenzi'
            },
            {
              label: 'Baasha'
            },
            {
              label: 'Zaka'
            }
          ]
        }
      ],
      dataset: [
        {
          seriesname: 'Ahadi',
          data: [
            {
              value: '125000'
            },
            {
              value: '300000'
            },
            {
              value: '480000'
            }
          ]
        },
        {
          seriesname: 'Makusanyo',
          data: [
            {
              value: '70000'
            },
            {
              value: '150000'
            },
            {
              value: '350000'
            }
          ]
        }
      ]
    };
  }

  ngOnInit() {}
}
