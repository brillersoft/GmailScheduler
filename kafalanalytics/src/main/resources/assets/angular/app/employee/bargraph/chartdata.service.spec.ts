import { TestBed, inject } from '@angular/core/testing';

import { ChartdataService } from './chartdata.service';

describe('ChartdataService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ChartdataService]
    });
  });

  it('should be created', inject([ChartdataService], (service: ChartdataService) => {
    expect(service).toBeTruthy();
  }));
});
