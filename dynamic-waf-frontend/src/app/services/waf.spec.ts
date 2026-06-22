import { TestBed } from '@angular/core/testing';

import { Waf } from './waf';

describe('Waf', () => {
  let service: Waf;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Waf);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
