import { TestBed } from '@angular/core/testing';

import { ApiCategory } from './api-category';

describe('ApiCategory', () => {
  let service: ApiCategory;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApiCategory);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
