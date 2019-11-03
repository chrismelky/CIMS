import { SpyObject } from './spyobject';
import { ChurchTrackerService } from 'app/core/tracker/tracker.service';

export class MockTrackerService extends SpyObject {
  constructor() {
    super(ChurchTrackerService);
  }

  connect() {}
}
