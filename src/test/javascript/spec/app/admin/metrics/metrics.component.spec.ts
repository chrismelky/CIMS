import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { of } from 'rxjs';

import { ChurchTestModule } from '../../../test.module';
import { ChurchMetricsMonitoringComponent } from 'app/admin/metrics/metrics.component';
import { ChurchMetricsService } from 'app/admin/metrics/metrics.service';

describe('Component Tests', () => {
  describe('ChurchMetricsMonitoringComponent', () => {
    let comp: ChurchMetricsMonitoringComponent;
    let fixture: ComponentFixture<ChurchMetricsMonitoringComponent>;
    let service: ChurchMetricsService;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [ChurchTestModule],
        declarations: [ChurchMetricsMonitoringComponent]
      })
        .overrideTemplate(ChurchMetricsMonitoringComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(ChurchMetricsMonitoringComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChurchMetricsService);
    });

    describe('refresh', () => {
      it('should call refresh on init', () => {
        // GIVEN
        const response = {
          timers: {
            service: 'test',
            unrelatedKey: 'test'
          },
          gauges: {
            'jcache.statistics': {
              value: 2
            },
            unrelatedKey: 'test'
          }
        };
        spyOn(service, 'getMetrics').and.returnValue(of(response));

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(service.getMetrics).toHaveBeenCalled();
      });
    });
  });
});
