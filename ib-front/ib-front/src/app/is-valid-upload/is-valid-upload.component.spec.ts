import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IsValidUploadComponent } from './is-valid-upload.component';

describe('IsValidUploadComponent', () => {
  let component: IsValidUploadComponent;
  let fixture: ComponentFixture<IsValidUploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IsValidUploadComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IsValidUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
